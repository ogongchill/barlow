# Claude 자율 개발 파이프라인 — Deep Dive

> 이 문서는 Claude가 로드된 시점부터 PR 생성까지 **어떻게 동작하는지**를 설계 의도와 함께 상세하게 기록한다.
> 대상 독자: 이 시스템을 유지·개선하거나 새로운 프로젝트에 적용하려는 개발자.

---

## 목차

1. [전체 흐름 조감도](#1-전체-흐름-조감도)
2. [.claude 파일 활성화 메커니즘](#2-claude-파일-활성화-메커니즘)
3. [컨텍스트 최적화 전략](#3-컨텍스트-최적화-전략)
4. [Phase별 동작 상세](#4-phase별-동작-상세)
5. [참조 문서 로드 전략](#5-참조-문서-로드-전략)
6. [효율성 분석](#6-효율성-분석)
7. [설계 트레이드오프](#7-설계-트레이드오프)

---

## 1. 전체 흐름 조감도

```
╔═════════════════════════════════════════════════════════════════════╗
║  Claude 세션 시작 — 자동 주입 (파일 로드 없이 컨텍스트에 삽입)                  ║
║  ├── [CLAUDE.md]          프로젝트 지침 (system-reminder)              ║
║  ├── [memory/MEMORY.md]   이전 세션 축적 상태                           ║
║  └── git status           현재 브랜치 & 최근 커밋                        ║
╚══════════════════════════════╤══════════════════════════════════════╝
                               │
                   이슈/요구사항 접수 또는 커맨드 실행
                               │
             ┌─────────────────┼──────────────────────────┐
             │ 일반 이슈 진입    │                           │ pipeline 커맨드 진입
             ▼                 │                           ▼
   ┌─────────────────┐         │               ┌──────────────────────┐
   │ 스킬 로드          │         │               │ pipeline/ 커맨드       │
   │                 │         │               │                      │
   │ [workflow-      │         │               │ [arch-plan.md]       │
   │  guide/         │         │               │ [arch-redesign.md]   │
   │  SKILL.md]      │         │               │ [arch-design.md]     │
   │ [coding-        │         │               │                      │
   │  rules/         │         │               │ → docs/plans/ 산출물  │
   │  SKILL.md]      │         │               │ → docs/adr/ 산출물    │
   └────────┬────────┘         │               │   ([templates/      │
            │                  │               │    adr.md] 참조)     │
            └──────────────────┘               └──────────────────────┘
                           ▼
          ┌───────────────────────────────────┐
          │   Phase 1~2: 분석 & 계획            │
          │   [WORKFLOW-PLAN.md] 명시적 읽기     │
          │                                   │
          │   coding-rules 스킬 로드 완료         │
          │      → [ARCHITECTURE.md]           │
          │      → [DOMAIN_RULES.md]     (작업  │
          │      → [SERVICE_RULES.md]    유형별 │
          │      → [ERROR_HANDLING.md]   선택)  │
          │   1. 요구사항 분석                    │
          │      → [DOMAIN_ENCYCLOPEDIA.md]    │
          │         BC 섹션 단위 로드             │
          │   2. 코드베이스 탐색                   │
          │   3. plan.md 작성 & 사용자 승인        │
          │      → [.claude/workspace/         │
          │          plan.md] 작성              │
          └────────────┬──────────────────────┘
                       │
                       ▼
          ┌───────────────────────────────────┐
          │   Phase 3: 코드 구현                │
          │   [WORKFLOW-IMPL.md] 명시적 읽기     │
          │                                   │
          │   domain → service                │
          │   → infra:storage → app:api       │
          └────────────┬──────────────────────┘
                       │
                       ▼
          ┌────────────────────────────────────┐
          │   Phase 4: 테스트 & 검증              │
          │   [WORKFLOW-VERIFY.md] 명시적 읽기    │
          │                                    │
          │   Agent 병렬 실행:                    │
          │   [항상] test-app-api           ─┐  │
          │   [core:domain 변경] test-core-  │  │
          │    domain                        ├─ │
          │   [core:service 변경] test-core- │  │
          │    service                      ─┘  │
          │   [선택] test-infra-auth             │
          │   [선택] test-infra-notification     │
          │   [선택] test-app-api-docs           │
          │                                    │
          │   spotlessApply → unitTest         │
          │   → contextTest → /harness-check   │
          └────────────┬────────────────────────┘
                       │
                       ▼
          ┌────────────────────────────────────┐
          │   Phase 5: 커밋 & PR                │
          │   [WORKFLOW-SHIP.md] 명시적 읽기     │
          │                                    │
          │   레이어별 커밋 분리                   │
          │   브랜치 생성 & PR 오픈               │
          └────────────┬───────────────────────┘
                       ▼
                 다음 이슈 시작
```

---

## 2. .claude 파일 활성화 메커니즘

`.claude/` 하위의 모든 `.md` 파일은 세 가지 방식 중 하나로 활성화된다.

### 2-1. 자동 주입 (Auto-Inject)

Claude 세션 시작 시 시스템이 자동으로 컨텍스트에 삽입. 명시적인 읽기 요청 불필요.

```
CLAUDE.md          ← system-reminder로 항상 주입
memory/MEMORY.md   ← user-memory로 항상 주입 (200줄 상한)
```

**특징**: 파일을 "읽는" 것이 아니라 컨텍스트에 직접 삽입됨. 컨텍스트 슬롯을 항상 점유.

---

### 2-2. 스킬 로드 → SKILL.md 자동 읽기

`workflow-guide` 또는 `coding-rules` 스킬이 Skill 툴로 실행되면 해당 `SKILL.md`가 로드된다.

```
Skill("workflow-guide") 실행
  → skills/workflow-guide/SKILL.md 자동 읽기
  → 이후 어떤 WORKFLOW-*.md를 읽어야 할지는 SKILL.md가 지시

Skill("coding-rules") 실행
  → skills/coding-rules/SKILL.md 자동 읽기
  → 작업 유형 판단 → 해당 규칙 파일 선택적 추가 읽기
```

**SKILL.md의 역할**: 전체 규칙의 압축 요약 + "지금 어떤 파일을 더 읽어야 하는가"를 결정하는 진입점.

---

### 2-3. 명시적 읽기 (Explicit Read)

Claude가 특정 Phase에 진입하거나 조건이 충족될 때 Read 툴로 직접 읽는다.

```
Phase 진입 트리거:
  Phase 1~2 진입 → WORKFLOW-PLAN.md 읽기
  Phase 3 진입   → WORKFLOW-IMPL.md 읽기
  Phase 4 진입   → WORKFLOW-VERIFY.md 읽기
  Phase 5 진입   → WORKFLOW-SHIP.md 읽기

조건 트리거:
  도메인 클래스 작성    → ARCHITECTURE.md + DOMAIN_RULES.md 읽기
  서비스/API 작성      → SERVICE_RULES.md 읽기
  예외 추가/수정       → ERROR_HANDLING.md 읽기
  BC 식별됨            → DOMAIN_ENCYCLOPEDIA.md 해당 BC 섹션 읽기
  arch-redesign 보고서 → templates/arch-redesign-report.md 읽기
  arch-design 보고서   → templates/arch-design-report.md 읽기
```

---

### 2-4. 커맨드 실행 → commands/*.md 자동 로드

`/project:` 접두사를 가진 슬래시 커맨드가 실행되면 해당 `.md` 파일이 Skill 툴을 통해 로드된다.

```
/harness-check     → commands/harness-check.md

/pipeline:arch-plan      → commands/pipeline/arch-plan.md
/pipeline:arch-redesign  → commands/pipeline/arch-redesign.md
/pipeline:arch-design    → commands/pipeline/arch-design.md
```

**pipeline/ 커맨드**: 파이프라인 전체를 대체하는 대형 커맨드. `arch-plan`은 탐색→계획→plan.md까지를 한 번에 처리. ADR/보고서 출력 시 `templates/` 파일을 참조.

---

### 2-5. Agent 스폰 → agents/*.md 프롬프트 로드

Phase 4에서 Agent 툴로 테스트 에이전트를 생성하면 해당 `agents/*.md`가 에이전트 시스템 프롬프트로 사용된다. 메인 Claude 컨텍스트는 소비하지 않는다.

```
Agent("test-core-domain")       → agents/test-core-domain.md
Agent("test-app-api")           → agents/test-app-api.md
Agent("test-core-service")      → agents/test-core-service.md
Agent("test-infra-auth")        → agents/test-infra-auth.md
Agent("test-infra-notification") → agents/test-infra-notification.md
Agent("test-app-api-docs")      → agents/test-app-api-docs.md
```

**핵심**: 에이전트는 독립 프로세스. 메인 세션 컨텍스트를 오염시키지 않고 병렬로 테스트 코드를 작성한다.

---

### 2-6. 파일 유형별 활성화 방식 요약

```
파일                              활성화 방식
────────────────────────────────────────────────────────────────
CLAUDE.md                         자동 주입 (항상)
memory/MEMORY.md                  자동 주입 (항상)
skills/*/SKILL.md                 스킬 로드 시 자동
skills/workflow-guide/WORKFLOW-*  명시적 읽기 (Phase 진입)
skills/coding-rules/ARCHITECTURE  명시적 읽기 (작업 유형)
skills/coding-rules/DOMAIN_RULES  명시적 읽기 (작업 유형)
skills/coding-rules/SERVICE_RULES 명시적 읽기 (작업 유형)
skills/coding-rules/ERROR_HANDLING 명시적 읽기 (작업 유형)
skills/coding-rules/TESTING       명시적 읽기 (작업 유형)
commands/harness-check.md         커맨드 실행 + 명시적 읽기
commands/pipeline/*.md            커맨드 실행 시 자동
agents/*.md                       Agent 스폰 시 (에이전트 프롬프트)
templates/*.md                    명시적 읽기 (보고서/ADR 작성 시)
docs/DOMAIN_ENCYCLOPEDIA.md       명시적 읽기 (BC 섹션 단위)
.claude/workspace/plan.md         명시적 읽기 (Phase 3 시작 시 복원)
────────────────────────────────────────────────────────────────
```

---

## 3. 컨텍스트 최적화 전략

Claude의 컨텍스트 창은 유한하다. 모든 규칙 파일을 세션 시작에 한번에 로드하면 실제 구현에 쓸 공간이 줄어든다. 이 파이프라인은 **세 가지 메커니즘**으로 컨텍스트를 최적화한다.

### 3-1. 지연 로드 (Lazy Loading)

```
규칙 파일 총량: ~5개 × 평균 300줄 = 약 1,500줄

세션 시작 시 로드:
  CLAUDE.md (약 80줄)       ← 항상 주입 (system-reminder)
  MEMORY.md (200줄 상한)    ← 항상 주입 (user-memory)
  workflow-guide/SKILL.md   ← 스킬 로드 시

Phase 진입 시 로드 (해당 시점에만):
  WORKFLOW-PLAN.md    ← Phase 1~2 진입 시
  WORKFLOW-IMPL.md    ← Phase 3 진입 시
  WORKFLOW-VERIFY.md  ← Phase 4 진입 시
  WORKFLOW-SHIP.md    ← Phase 5 진입 시
```

**효과**: 어느 시점에도 동시에 로드되는 Phase 파일은 최대 1개. 전체를 미리 로드하는 방식 대비 약 60~70% 컨텍스트 절감.

### 3-2. BC 섹션 단위 지연 로드 (DOMAIN_ENCYCLOPEDIA)

```
docs/DOMAIN_ENCYCLOPEDIA.md: 전량 로드 시 대용량

로드 전략:
  이슈에서 BC명이 명확히 식별됨 → 해당 BC 섹션만 읽기
  BC 불명확 / 신규 BC / BC 간 의존성 확인 필요 → 전량 로드
```

**근거**: 대부분의 이슈는 1~2개 BC에 집중된다. BC 이름이 이슈에 명시되어 있으면 전체 Encyclopedia를 읽을 이유가 없다.

### 3-3. MEMORY.md — 세션 간 상태 전달

```
MEMORY.md 역할:
  # currentWork 섹션: 현재 진행 중인 이슈, 브랜치, 단계
  → 새 세션에서 "어디까지 했나" 즉시 파악 가능

업데이트 시점:
  Phase 2 승인 후:   currentWork 섹션 작성 + 구현 진행 중 기록
  Phase 5 완료 후:   currentWork 섹션 전체 삭제
```

---

## 4. Phase별 동작 상세

### Phase 1~2: 분석 & 계획

**로드 파일**: `WORKFLOW-PLAN.md`

**Phase 1 — 요구사항 분석**:
1. `docs/DOMAIN_ENCYCLOPEDIA.md` 해당 BC 섹션 읽기
2. 관련 기존 코드 탐색 — 동일 BC 패턴, 유사 구현체, Port Interface
3. 브랜치 확인: `feat/issue/{번호}` (develop에서 분기)

**Phase 2 — plan.md 작성 & 승인**:
- `.claude/workspace/plan.md` 작성 (매 작업마다 덮어쓴다)
- **승인 없이 코드 작성 절대 금지**
- 승인 직후 MEMORY.md `# currentWork` 섹션 작성

**plan.md 구조**:
```markdown
# 구현 계획 — #{번호} {제목}

## 변경 범위
- [ ] `{파일경로}`: {변경 내용 한 줄}

## 설계 결정
- {결정 사항}: {선택 이유}

## 커밋 계획
1. `feat : {내용}` — {대상 파일}
2. `test : {내용}` — {대상 파일}
3. `lint : lint 적용`
```

**MEMORY.md currentWork 형식**:
```markdown
# currentWork — #{번호} {제목}
브랜치: feat/issue/{번호}

- [x] 분석 + plan.md 작성
- [x] 승인
- [ ] 코드 구현
- [ ] test agent 실행
- [ ] spotlessApply + contextTest + harness-check
- [ ] 커밋
- [ ] PR
```

---

### Phase 3: 코드 구현

**로드 파일**: `WORKFLOW-IMPL.md`

**구현 순서** (의존성 방향 기반):
```
1. core:domain    → AR / VO / Command / Query / Repository Interface / Exception
2. core:service   → Implement Layer (Reader/Manager/Activator 등)
3. core:service   → Business Layer (Service/Facade)
4. infra:storage  → JpaEntity / JpaRepository / RepositoryAdapter
5. app:api        → Controller / Request / Response
```

**각 레이어 완료 시 체크리스트 실행**:

| 레이어 | 핵심 체크 |
|---|---|
| core:domain | setter 없음 / Spring 어노테이션 없음 / AR 간 ID-only 참조 |
| core:service Business | @Transactional 선언 / infra:* 직접 주입 없음 / 다른 @Service 직접 주입 없음 |
| core:service Implement | Port Interface만 통해 infra 통신 |
| 예외 | CoreDomainException 하위 / static factory / BUSINESS vs IMPLEMENTATION 레벨 |
| app:api | Request → Command 변환 / Passport 파라미터로 인증 주입 |

---

### Phase 4: 테스트 & 검증

**로드 파일**: `WORKFLOW-VERIFY.md`

**테스트 전략의 핵심**:
- 비즈니스 레이어(`@Service`) Mock 단위 테스트 **작성 안 함** — AcceptanceTest가 end-to-end로 커버
- Repository 테스트 **별도 작성 안 함** — AcceptanceTest가 DB까지 커버

**Agent 실행 기준**:

| 조건 | Agent |
|---|---|
| **항상** | `test-app-api` |
| `core:domain` 변경 시 | `test-core-domain` |
| `core:service` 순수 로직 변경 시 | `test-core-service` |
| `app:api` 신규 엔드포인트 시 | `test-app-api-docs` |
| `infra:notification` 변경 시 | `test-infra-notification` |
| `infra:auth` 변경 시 | `test-infra-auth` |

**검증 순서**:
```bash
./gradlew spotlessApply                  # 린트 적용
./gradlew unitTest                       # 도메인 정책 단위 테스트
./gradlew contextTest                    # 인수 테스트 (DB 포함)
/harness-check                           # 아키텍처 규칙 최종 관문
```

**Harness 검증** (`/harness-check`):
- 이번 세션 전체 변경 파일 대상
- Architecture / Domain / Service / Error Handling / Testing 5개 영역 체크
- FAIL 1개라도 있으면 커밋 진행 불가. **동일 명령 반복 재시도 금지** — 원인 분석 후 코드 수정.

---

### Phase 5: 커밋 & PR

**로드 파일**: `WORKFLOW-SHIP.md`

**커밋 분리 원칙**:
```bash
git add core/domain/...
git commit -m "feat : {BC명} {기능} 도메인 모델 추가"    # core:domain

git add core/service/...
git commit -m "feat : {BC명} {기능} 서비스 레이어 구현"  # core:service

git add infra/...
git commit -m "feat : {BC명} {기능} 어댑터 구현"         # infra:storage

git add app/...
git commit -m "feat : {BC명} {기능} API 엔드포인트 추가"  # app:api

git add **/test/...
git commit -m "test : {BC명} {기능} 인수 테스트 추가"    # 테스트

./gradlew spotlessApply
git add -A
git commit -m "lint : lint 적용"                         # 항상 마지막
```

**PR 생성**:
```bash
# 브랜치: feat/issue/{번호} (항상 단일 브랜치, develop에서 분기)
gh pr create \
  --base develop \
  --title "{type}: {한국어 설명}" \
  --body "$(cat .github/PULL_REQUEST_TEMPLATE.md)"
```

PR 제목: 70자 이하. 예) `feat: 알림센터 7일 초과 항목 삭제 배치 잡 추가`

**완료 후**: MEMORY.md `# currentWork` 섹션 전체 삭제.

**금지사항**:
- `git push --force` 금지
- harness-check PASS 없이 커밋 금지
- BC 경계·API 계약 변경 여부가 불명확하면 코드 작성 멈추고 사용자에게 확인

---

## 5. 참조 문서 로드 전략

총 10개의 참조 문서가 존재하지만, 한 Phase에서 동시에 로드되는 문서는 최대 3~4개.

### 전체 문서 맵

```
.claude/
  skills/
    workflow-guide/
      SKILL.md            ← 스킬 로드 시 자동 (요약)
      WORKFLOW.md         ← 전체 구조 파악 필요 시만
      WORKFLOW-PLAN.md    ← Phase 1~2에서만 (명시적 읽기)
      WORKFLOW-IMPL.md    ← Phase 3에서만 (명시적 읽기)
      WORKFLOW-VERIFY.md  ← Phase 4에서만 (명시적 읽기)
      WORKFLOW-SHIP.md    ← Phase 5에서만 (명시적 읽기)
    coding-rules/
      SKILL.md            ← 스킬 로드 시 자동 (요약 + 파일 맵 + 핵심 5규칙)
      ARCHITECTURE.md     ← 도메인/서비스 코드 작성 시 (명시적 읽기)
      DOMAIN_RULES.md     ← 도메인 클래스 작성 시 (명시적 읽기)
      SERVICE_RULES.md    ← 서비스/API 작성 시 (명시적 읽기)
      ERROR_HANDLING.md   ← 예외 추가/수정 시 (명시적 읽기)
      TESTING.md          ← 테스트 작성 시 (명시적 읽기)
  commands/
    harness-check.md      ← Phase 4 최종 관문 / /harness-check 커맨드
    pipeline/
      arch-plan.md        ← /pipeline:arch-plan 커맨드 실행 시 자동
      arch-redesign.md    ← /pipeline:arch-redesign 커맨드 실행 시 자동
      arch-design.md      ← /pipeline:arch-design 커맨드 실행 시 자동
  agents/
    test-core-domain.md       ← Phase 4: Agent 스폰 시 (에이전트 프롬프트)
    test-app-api.md            ← Phase 4: Agent 스폰 시 (항상)
    test-core-service.md       ← Phase 4: Agent 스폰 시 (core:service 변경 시)
    test-infra-auth.md         ← Phase 4: Agent 스폰 시 (infra:auth 변경 시)
    test-infra-notification.md ← Phase 4: Agent 스폰 시 (infra:notification 변경 시)
    test-app-api-docs.md       ← Phase 4: Agent 스폰 시 (API 엔드포인트 변경 시)
  templates/
    adr.md                  ← ADR 작성 시 (명시적 읽기)
    arch-redesign-report.md ← /arch-redesign 보고서 생성 시 (명시적 읽기)
    arch-design-report.md   ← /arch-design 보고서 생성 시 (명시적 읽기)

docs/
  DOMAIN_ENCYCLOPEDIA.md  ← Phase 1에서 BC 섹션 단위 지연 로드 (명시적 읽기)

.claude/workspace/          ← 세션 간 상태 전달용 파일 (Claude가 직접 작성)
  plan.md                   ← Phase 2 완료 후 작성 → Phase 3 시작 시 참조
```

### Phase별 실제 로드 패턴

```
[스킬 로드]  workflow-guide/SKILL.md + coding-rules/SKILL.md
Phase 1~2:  WORKFLOW-PLAN.md + ARCHITECTURE.md + DOMAIN_RULES.md (작업 유형별)
            + DOMAIN_ENCYCLOPEDIA.md (해당 BC 섹션)
Phase 3:    WORKFLOW-IMPL.md + (필요 시 coding-rules 파일)
Phase 4:    WORKFLOW-VERIFY.md
Phase 5:    WORKFLOW-SHIP.md
```

### SKILL.md의 역할 — "목차 압축"

각 스킬의 `SKILL.md`는 전체 내용의 압축 요약본이다.

```
workflow-guide/SKILL.md:
  → 각 Phase 파일이 무엇을 담는지 한 눈에 파악
  → "어떤 Phase에 있는가"에 따라 어떤 파일을 읽어야 할지 즉시 결정 가능

coding-rules/SKILL.md:
  → 핵심 5규칙 요약 (의존성, 도메인, Business Layer, 예외, AR 참조)
  → 작업 유형 → 로드할 파일 목록 제공
  → 전체 규칙 파일을 읽지 않아도 주요 원칙을 즉시 상기 가능
```

**설계 의도**: SKILL.md를 읽는 것만으로도 "어떤 파일을 더 읽어야 하는지"를 판단할 수 있다. 불필요한 전량 로드를 방지하는 진입점 역할.

---

## 6. 효율성 분석

### 6-1. Agent 병렬 실행의 효과 (Phase 4)

```
순차 실행:
  test-app-api      (7분)
  test-core-domain  (5분)
  test-core-service (4분)
  합계: 약 16분

병렬 실행 (Agent 툴 사용):
  [동시 실행] test-app-api + test-core-domain + test-core-service
  합계: 최대 시간 = 7분 (병목 기준)

효과: 약 56% 시간 단축
```

병렬 실행이 가능한 이유: 각 모듈(core:domain, app:api, core:service)이 독립적이라 파일 충돌이 없다.

### 6-2. harness-check 위치

```
구현 도중 체크 (매 파일마다): 오버헤드 과다
커밋 후 체크: 이미 이력이 남음, 수정 커밋 발생
Phase 4 완료 직후 (커밋 전): 최적 위치

Harness가 FAIL을 잡아내는 범주:
  - 레이어 의존성 위반 (core:domain에 @Entity)
  - 불변 규칙 위반 (setter 존재)
  - 예외 계층 위반 (RuntimeException 직접 상속)
  - 트랜잭션 규칙 위반 (@Component에 @Transactional)
```

### 6-3. plan.md 단일화의 효과

```
이전 방식 (탐색결과.md + 구현계획.md 분리):
  → 두 파일을 각각 작성·관리·재로드
  → 세션 격리 후 두 파일 모두 읽어야 복원 완료

현재 방식 (plan.md 단일 파일):
  → 한 파일에 변경 범위 + 설계 결정 + 커밋 계획 통합
  → 유지 비용 감소, 승인 흐름 단순화
```

---

## 7. 설계 트레이드오프

### 7-1. 파일 분산 vs 단일 파일

**현재 선택**: Phase별 파일 분산 (4개 파일)
**대안**: 단일 WORKFLOW.md에 전체 내용

| 항목 | 분산 | 단일 |
|---|---|---|
| 컨텍스트 사용량 | 낮음 (필요 Phase만 로드) | 높음 (항상 전체 로드) |
| 탐색 편의성 | 낮음 (어떤 파일을 읽어야 하는지 판단 필요) | 높음 (한 파일에 전체) |
| 유지보수 | 낮음 (파일 간 참조 관계 관리 필요) | 높음 (한 곳만 수정) |

컨텍스트 효율이 가장 중요한 제약이므로 **분산 구조** 채택.

### 7-2. Tier 시스템을 제거한 이유

이전에는 Phase -1에서 이슈를 Tier 1/2/3으로 분류하여 진행 방식을 달리했다. 제거 이유:

- **tier/ 커맨드의 실용성 낮음**: 대부분의 이슈가 plan.md 작성 → 승인 → 구현으로 수렴됨
- **Tier 3 진입로 충분**: `pipeline:arch-plan` / `pipeline:arch-redesign` / `pipeline:arch-design` 커맨드가 아키텍처 분석 전용 경로를 담당
- **단순화 이득**: Phase 번호 체계 정리, 진입 조건 판단 오버헤드 제거

**현재 방식**: 모든 일반 이슈는 Phase 1~2(분석+plan.md 승인) → Phase 3(구현) → Phase 4(검증) → Phase 5(커밋+PR) 단일 경로로 처리.

### 7-3. test-app-api를 "항상" 실행하는 이유

```
이전: AcceptanceTest가 있는 경우에만 실행
현재: 항상 실행

근거:
  - AcceptanceTest는 비즈니스 서비스(@Service)의 end-to-end 검증 수단
  - API 변경이 없어도 도메인/서비스 변경이 기존 API 동작에 영향을 줄 수 있음
  - 회귀 방지를 위한 최소 안전망
```

---

## 부록: 핵심 설계 원칙 요약

| 원칙 | 구현 방식 |
|---|---|
| 컨텍스트는 희소 자원 | Phase별 지연 로드, BC 섹션 단위 로드 |
| 방향이 먼저, 구현은 나중 | plan.md 승인 → 구현 순서 강제 |
| 아키텍처는 자동 검증 | Harness 체크리스트를 커밋 전 필수 관문으로 배치 |
| 실수는 앞단에서 잡는다 | 구현 체크리스트(Phase 3) + Harness(Phase 4) 이중 검증 |
| 병렬 가능한 것은 병렬로 | 테스트 Agent 병렬 실행 (Phase 4) |
| 세션 상태는 파일로 저장 | MEMORY.md + workspace/plan.md |