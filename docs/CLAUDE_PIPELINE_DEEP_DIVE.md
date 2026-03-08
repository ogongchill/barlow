# Claude 자율 개발 파이프라인 — Deep Dive

> 이 문서는 Claude가 로드된 시점부터 PR 생성까지 **어떻게 동작하는지**를 설계 의도와 함께 상세하게 기록한다.
> 대상 독자: 이 시스템을 유지·개선하거나 새로운 프로젝트에 적용하려는 개발자.

---

## 목차

1. [전체 흐름 조감도](#1-전체-흐름-조감도)
2. [컨텍스트 최적화 전략](#2-컨텍스트-최적화-전략)
3. [Phase별 동작 상세](#3-phase별-동작-상세)
4. [참조 문서 로드 전략](#4-참조-문서-로드-전략)
5. [효율성 분석](#5-효율성-분석)
6. [설계 트레이드오프](#6-설계-트레이드오프)

---

## 1. 전체 흐름 조감도

```
┌─────────────────────────────────────────────────────────────────┐
│  Claude 세션 시작                                                 │
│  ├── system-reminder: CLAUDE.md (프로젝트 지침) 자동 주입            │
│  ├── memory: MEMORY.md (이전 세션 컨텍스트) 자동 주입                 │
│  └── git status: 현재 브랜치 & 최근 커밋 자동 주입                     │
└────────────────────────┬────────────────────────────────────────┘
                         │
                    이슈/요구사항 접수
                         │
                         ▼
┌─────────────────────────────────────────────────────────────────┐
│  스킬 로드 (지연 로드)                                               │
│  1. workflow-guide  →  SKILL.md (요약 + Phase 파일 맵)              │
│                        이슈 접수 시 → WORKFLOW-TRIAGE.md 추가 로드   │
│  2. coding-rules    →  SKILL.md (복잡도 판단 + 작업 유형별 파일 맵)   │
│                        Phase 0 작업 유형 확정 후 → 해당 규칙 파일 추가 │
└────────────────────────┬────────────────────────────────────────┘
                         │
                         ▼
         ┌───────────────────────────────┐
         │   Phase -1: Tier 판정          │
         │   WORKFLOW-TRIAGE.md 참조      │
         └───────────┬───────────────────┘
          ┌──────────┼──────────┐
          ▼          ▼          ▼
       Tier 1      Tier 2     Tier 3
      자율 진행  계획 후 승인  분석만
          │          │          │
          │          │          └── ADR 작성 후 종료
          │          │
          └──────────┤
                     ▼
         ┌───────────────────────────────┐
         │   Phase 0~3: 분석 & 계획        │
         │   WORKFLOW-PLAN.md 참조        │
         │                               │
         │   0. 스킬 & 규칙 파일 로드        │
         │   1. 요구사항 분석               │
         │   2. 코드베이스 탐색             │
         │   3. 구현 계획 작성              │
         └───────────┬───────────────────┘
                     │ [복잡 작업: /clear → 세션 격리 체크포인트 A]
                     │ [단순 작업: 연속 진행]
                     ▼
         ┌───────────────────────────────┐
         │   Phase 4: 코드 구현            │
         │   WORKFLOW-IMPL.md 참조        │
         │                               │
         │   domain → service            │
         │   → infra:storage → app:api   │
         └───────────┬───────────────────┘
                     │
                     ▼
         ┌───────────────────────────────┐
         │   Phase 5~6: 테스트 & 검증       │
         │   WORKFLOW-VERIFY.md 참조      │
         │                               │
         │   5. Agent 병렬 테스트 작성       │
         │   6. 린트 → CI 테스트            │
         │      → Harness 검증 (/harness) │
         └───────────┬───────────────────┘
                     │ [세션 격리 권장: Harness PASS 후 /clear → 체크포인트 B]
                     ▼
         ┌───────────────────────────────┐
         │   Phase 7~8: 커밋 & PR         │
         │   WORKFLOW-SHIP.md 참조        │
         │                               │
         │   7. 레이어별 커밋 분리            │
         │   8. 브랜치 생성 & PR 오픈        │
         └───────────┬───────────────────┘
                     ▼
               다음 이슈 시작
```

---

## 2. 컨텍스트 최적화 전략

Claude의 컨텍스트 창은 유한하다. 모든 규칙 파일을 세션 시작에 한번에 로드하면 실제 구현에 쓸 공간이 줄어든다. 이 파이프라인은 **네 가지 메커니즘**으로 컨텍스트를 최적화한다.

### 2-1. 지연 로드 (Lazy Loading)

```
규칙 파일 총량: ~5개 × 평균 300줄 = 약 1,500줄

세션 시작 시 로드:
  CLAUDE.md (약 80줄)       ← 항상 주입 (system-reminder)
  MEMORY.md (200줄 상한)    ← 항상 주입 (user-memory)
  workflow-guide/SKILL.md   ← 스킬 로드 시

Phase 진입 시 로드 (해당 시점에만):
  WORKFLOW-TRIAGE.md  ← Phase -1 진입 시
  WORKFLOW-PLAN.md    ← Phase 0~3 진입 시
  WORKFLOW-IMPL.md    ← Phase 4 진입 시
  WORKFLOW-VERIFY.md  ← Phase 5~6 진입 시
  WORKFLOW-SHIP.md    ← Phase 7~8 진입 시
  TESTING.md          ← Phase 5 진입 직전 (Phase 0에서 선취 금지)
```

**효과**: 어느 시점에도 동시에 로드되는 Phase 파일은 최대 1~2개. 전체를 미리 로드하는 방식 대비 약 60~70% 컨텍스트 절감.

### 2-2. BC 섹션 단위 지연 로드 (DOMAIN_ENCYCLOPEDIA)

```
docs/DOMAIN_ENCYCLOPEDIA.md: 전량 로드 시 대용량

로드 전략:
  이슈에서 BC명이 명확히 식별됨 → 해당 BC 섹션만 읽기
  BC 불명확 / 신규 BC / BC 간 의존성 확인 필요 → 전량 로드
```

**근거**: 대부분의 이슈는 1~2개 BC에 집중된다. BC 이름이 이슈에 명시되어 있으면 전체 Encyclopedia를 읽을 이유가 없다.

### 2-3. 세션 격리 (Session Isolation)

가장 강력한 컨텍스트 최적화 기법. 탐색·분석 과정에서 쌓인 대화 이력이 구현 단계에서 노이즈가 되는 것을 방지한다.

```
체크포인트 A (Phase 3→4, 복잡 작업만)
  - 조건: 구현계획.md + 탐색결과.md 파일이 모두 작성된 경우
  - 방법: /clear 후 구현계획.md + 탐색결과.md를 읽어 컨텍스트 복원
  - 효과: 수백 줄의 탐색 대화 이력 → 필요한 정보만 담긴 파일 2개로 압축

체크포인트 B (Phase 6→7, 권장)
  - 조건: 전체 테스트 PASS + Harness PASS 확인 후
  - 효과: 구현·디버깅 이력 제거, 커밋/PR 작성에 집중
```

**단순 작업 예외**: 변경 파일 5개 미만인 작업은 /clear 없이 연속 진행. 세션 격리 비용(재로드 시간)이 절감 효과보다 크기 때문.

### 2-4. MEMORY.md — 세션 간 상태 전달

세션 격리는 대화 이력을 날리지만, 작업 상태는 보존해야 한다.

```
MEMORY.md 역할:
  # currentWork 섹션: 현재 진행 중인 이슈, 브랜치, 단계, 파일 목록
  → 세션 격리 후 새 세션에서 "어디까지 했나" 즉시 파악 가능

업데이트 시점:
  Phase 2 완료 후: 탐색결과.md 작성됨 기록
  Phase 3 승인 후: 구현 진행 중 + 변경 범위 기록
  Phase 6 완료 후: Phase 6 완료 기록 (체크포인트 B 직전)
  Phase 7 완료 후: 커밋 완료 단계로 업데이트
  Phase 8 완료 후: currentWork 섹션 전체 삭제
```

---

## 3. Phase별 동작 상세

### Phase -1: Tier 판정

**로드 파일**: `WORKFLOW-TRIAGE.md`

5가지 판단 축으로 이슈를 분류한다:

| 축 | 평가 방법 |
|---|---|
| 변경 방향 | 순수 추가인가 vs 기존 동작 수정/삭제인가 |
| 아키텍처 영향 | 단일 BC인가 vs BC 경계 변경인가 |
| 가역성 | 롤백 가능한가 vs DB 스키마/API 계약 변경인가 |
| 요구사항 완결성 | 명확한 스펙인가 vs 탐색적인가 |
| 결정 주체 | 구현 방법 결정인가 vs 기술 방향 결정인가 |

**오분류 원칙**: 애매하면 항상 상위 Tier. Tier 1 vs 2 → Tier 2 선택.

**출력**: 채팅에 Tier 판정 결과 블록을 출력하고 진행 방식 명시.

---

### Phase 0: 스킬 & 규칙 로드

**목적**: 구현에 필요한 규칙 파일만 선택적으로 로드.

```
workflow-guide 로드 (이미 완료)
  ↓
coding-rules 로드
  ↓
DOMAIN_ENCYCLOPEDIA.md — BC 섹션 단위 지연 로드
  ↓
작업 유형 판단 → 해당 규칙 파일 추가 로드

  도메인 클래스 → ARCHITECTURE.md + DOMAIN_RULES.md
  core:service  → ARCHITECTURE.md + SERVICE_RULES.md
  API 엔드포인트 → SERVICE_RULES.md
  예외          → ERROR_HANDLING.md
  테스트 (단독) → TESTING.md
  전체 기능     → 위 전부 (단, TESTING.md는 Phase 5 진입 시)
```

---

### Phase 1~3: 요구사항 분석 → 탐색 → 계획

**로드 파일**: `WORKFLOW-PLAN.md`

**Phase 1 — 요구사항 분석**:
1. 도메인 명사 추출 → DOMAIN_ENCYCLOPEDIA 빠른 참조 테이블에서 정확한 클래스명·패키지명 확인
2. 영향 BC 식별
3. 구현 위치 결정 (각 레이어별 파일 경로 사전 결정)
4. 복잡도 판단 → Phase 2/3 파일 작성 여부 결정
5. 브랜치 전략 결정 (단일 vs 서브 브랜치, 기준: 파일 수 12개)

**Phase 2 — 코드베이스 탐색**:
- 동일 BC의 기존 AR, Repository, Service 읽기
- 유사 구현체 패턴 파악
- [복잡 작업] `.claude/workspace/탐색결과.md` 작성

**Phase 3 — 구현 계획**:
- [단순 작업] 채팅에 계획 출력 후 승인 대기
- [복잡 작업] `.claude/workspace/구현계획.md` 작성 후 승인 대기
- **승인 없이 코드 작성 절대 금지** (Tier 2 이상)

**산출물 구조** (복잡 작업):
```
.claude/workspace/
  ├── 탐색결과.md   ← 영향 파일, 재사용 패턴, 신규 생성 항목
  └── 구현계획.md  ← 변경 범위, 구현 순서, 설계 결정, 커밋 계획, 브랜치 전략
```

---

### Phase 4: 코드 구현

**로드 파일**: `WORKFLOW-IMPL.md` (세션 격리 후 재로드 시)

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
| core:service | Business Layer: @Transactional 선언 / infra:* 직접 주입 없음 |
| 예외 | BC별 하위 클래스 / static factory / BUSINESS vs IMPLEMENTATION 레벨 |
| app:api | Request → Command 변환 / request.validate() 호출 |

---

### Phase 5~6: 테스트 & 검증

**로드 파일**: `WORKFLOW-VERIFY.md`, `TESTING.md` (Phase 5 진입 시 로드)

**테스트 전략의 핵심**:
- 비즈니스 레이어(`@Service`) Mock 단위 테스트 **작성 안 함** — AcceptanceTest가 end-to-end로 커버
- Repository 테스트 **별도 작성 안 함** — AcceptanceTest가 DB까지 커버

**Agent 병렬 실행** (Phase 5):
```
[항상] test-core-domain   ─┐
[항상] test-app-api        ├─ 동시 실행 (모듈이 다르므로 의존성 없음)
[해당 시] test-core-service ┘

[선택] test-infra-auth         ← infra:auth 변경 시
[선택] test-infra-notification ← infra:notification 변경 시
[선택] test-app-api-docs       ← API 엔드포인트 추가/변경 시
```

**Phase 6 검증 순서**:
```bash
./gradlew spotlessApply                  # 린트 적용
./gradlew unitTest                       # 도메인 정책 단위 테스트
./gradlew contextTest                    # 인수 테스트 (DB 포함)
./gradlew :core:domain:compileJava       # 컴파일 확인
./gradlew :core:service:compileJava
./gradlew :app:api:compileJava
/project:harness-check                   # 아키텍처 규칙 최종 관문
```

**Harness 검증** (`/harness-check`):
- 이번 세션 전체 변경 파일 대상
- Architecture / Domain / Service / Error Handling / Testing 5개 영역 체크
- FAIL 1개라도 있으면 커밋 진행 불가

---

### Phase 7~8: 커밋 & PR

**로드 파일**: `WORKFLOW-SHIP.md` (세션 격리 B 후 재로드)

**커밋 분리 원칙**:
```bash
git commit -m "feat : {BC명} {기능} 도메인 모델 추가"    # core:domain
git commit -m "feat : {BC명} {기능} 서비스 레이어 구현"  # core:service
git commit -m "feat : {BC명} {기능} JPA 어댑터 구현"    # infra:storage
git commit -m "feat : {BC명} {기능} API 엔드포인트 추가" # app:api
git commit -m "test : {BC명} {기능} 테스트 추가"         # 테스트
git commit -m "lint : lint 적용"                         # 항상 마지막
```

**PR 크기 기준**:

| 기준 | 권장 | 최대 |
|---|---|---|
| 변경 파일 수 | ≤ 8개 | 12개 |
| 라인 추가(+) | ≤ 400줄 | 600줄 |

초과 시 레이어 단위 서브 브랜치 전략 적용:
```
develop
  └── feat/issue/{번호}              ← 빈 베이스
        ├── feat/issue/{번호}/domain  → PR → 베이스 [1]
        ├── feat/issue/{번호}/service → PR → 베이스 [2]
        ├── feat/issue/{번호}/storage → PR → 베이스 [3]
        └── feat/issue/{번호}/api     → PR → 베이스 [4]
      feat/issue/{번호}              → PR → develop  [5]
```

---

## 4. 참조 문서 로드 전략

총 12개의 참조 문서가 존재하지만, 한 Phase에서 동시에 로드되는 문서는 최대 3~4개.

### 전체 문서 맵

```
.claude/
  skills/
    workflow-guide/
      SKILL.md            ← 스킬 로드 시 자동 (요약)
      WORKFLOW.md         ← 전체 구조 파악 필요 시만
      WORKFLOW-TRIAGE.md  ← Phase -1에서만
      WORKFLOW-PLAN.md    ← Phase 0~3에서만
      WORKFLOW-IMPL.md    ← Phase 4에서만 (세션 격리 후 재로드)
      WORKFLOW-VERIFY.md  ← Phase 5~6에서만
      WORKFLOW-SHIP.md    ← Phase 7~8에서만 (세션 격리 후 재로드)
    coding-rules/
      SKILL.md            ← 스킬 로드 시 자동 (요약 + 파일 맵)
      ARCHITECTURE.md     ← 도메인/서비스 코드 작성 시
      DOMAIN_RULES.md     ← 도메인 클래스 작성 시
      SERVICE_RULES.md    ← 서비스/API 작성 시
      ERROR_HANDLING.md   ← 예외 추가/수정 시
      TESTING.md          ← Phase 5 진입 직전
  commands/
    harness-check.md      ← Phase 6 최종 관문 시
    tier/
      dev-auto.md         ← /tier:dev-auto 커맨드 실행 시
      dev-plan.md         ← /tier:dev-plan 커맨드 실행 시
      dev-analyze.md      ← /tier:dev-analyze 커맨드 실행 시
    pipeline/
      arch-plan.md        ← /pipeline:arch-plan 커맨드 실행 시
      arch-redesign.md    ← /pipeline:arch-redesign 커맨드 실행 시
      arch-design.md      ← /pipeline:arch-design 커맨드 실행 시

docs/
  DOMAIN_ENCYCLOPEDIA.md  ← Phase 0에서 BC 섹션 단위 지연 로드
```

### Phase별 실제 로드 패턴

```
[스킬 로드]  workflow-guide/SKILL.md + coding-rules/SKILL.md
Phase -1:   WORKFLOW-TRIAGE.md
Phase 0:    WORKFLOW-PLAN.md + ARCHITECTURE.md + DOMAIN_RULES.md (작업 유형별)
            + DOMAIN_ENCYCLOPEDIA.md (해당 BC 섹션)
Phase 4:    WORKFLOW-IMPL.md (세션 격리 후) + 구현계획.md + 탐색결과.md
Phase 5:    WORKFLOW-VERIFY.md + TESTING.md
Phase 6:    harness-check.md
Phase 7~8:  WORKFLOW-SHIP.md
```

### SKILL.md의 역할 — "목차 압축"

각 스킬의 `SKILL.md`는 전체 내용의 압축 요약본이다.

```
workflow-guide/SKILL.md (약 35줄):
  → 각 Phase 파일이 무엇을 담는지 한 눈에 파악
  → "어떤 Phase에 있는가"에 따라 어떤 파일을 읽어야 할지 즉시 결정 가능

coding-rules/SKILL.md (약 65줄):
  → 작업 유형 → 로드할 파일 매핑 테이블 제공
  → 핵심 원칙 요약 (Architecture, Domain, Error Handling, Testing 각 3~4줄)
  → 전체 규칙 파일을 읽지 않아도 주요 원칙을 즉시 상기 가능
```

**설계 의도**: SKILL.md를 읽는 것만으로도 "어떤 파일을 더 읽어야 하는지"를 판단할 수 있다. 불필요한 전량 로드를 방지하는 진입점 역할.

---

## 5. 효율성 분석

### 5-1. Tier 판정이 가져오는 효율

| Tier | 프로세스 | 오버헤드 | 적합한 경우 |
|---|---|---|---|
| Tier 1 | 판정 출력 → 즉시 구현 | 최소 | 단일 BC, 명확한 스펙 |
| Tier 2 | 계획.md → 승인 → 구현 | 계획 작성 1회 | 멀티 BC, 기존 동작 변경 |
| Tier 3 | 분석 보고서만 | 코드 작성 없음 | 아키텍처 결정 필요 |

Tier 1 작업을 Tier 3 프로세스로 처리하면: 분석 보고서 작성 → 아무 코드도 나오지 않음 → 낭비.
Tier 3 작업을 Tier 1로 처리하면: 잘못된 방향으로 코드 작성 → 대규모 재작업.

**Tier 판정은 낭비를 방지하는 가장 앞단의 필터.**

### 5-2. 세션 격리의 실제 효과

복잡한 Tier 2 작업 (변경 파일 10개, 탐색 깊이 깊음):

```
세션 격리 없이 진행:
  Phase 0~8까지 단일 세션 → 탐색 대화 200줄 + 구현 컨텍스트 500줄
  → 후반부에서 "앞에서 논의했던 내용"이 희석됨
  → 컨텍스트 압축 발생 가능 (긴 세션)

세션 격리 적용:
  Phase 3 완료 → /clear → 구현계획.md (약 80줄) + 탐색결과.md (약 60줄) 로드
  → Phase 4 시작 시 컨텍스트: 핵심 정보만 약 250줄
  → 구현 집중도 향상, 컨텍스트 압축 위험 제거
```

### 5-3. Agent 병렬 실행의 효과 (Phase 5)

```
순차 실행:
  test-core-domain  (5분)
  test-app-api      (7분)
  test-core-service (4분)
  합계: 약 16분

병렬 실행 (Agent 툴 사용):
  [동시 실행] test-core-domain + test-app-api + test-core-service
  합계: 최대 시간 = 7분 (병목 기준)

효과: 약 56% 시간 단축
```

병렬 실행이 가능한 이유: 각 모듈(core:domain, app:api, core:service)이 독립적이라 파일 충돌이 없다.

### 5-4. 브랜치 크기 제한의 효과

| PR 크기 | 리뷰 용이성 | 머지 안전성 |
|---|---|---|
| ≤ 8파일 / ≤ 400줄 | 높음 (리뷰어가 집중 가능) | 충돌 최소화 |
| 8~12파일 | 보통 | 허용 범위 |
| > 12파일 | 낮음 | 서브 브랜치 강제 |

서브 브랜치 전략은 리뷰 품질을 위한 강제 장치이지, 순수한 기술적 필요에 의한 것이 아니다.

### 5-5. Harness 검증의 위치

```
구현 도중 체크 (매 파일마다): 오버헤드 과다
커밋 후 체크: 이미 이력이 남음, 수정 커밋 발생
Phase 6 완료 직후 (커밋 전): 최적 위치

Harness가 FAIL을 잡아내는 범주:
  - 레이어 의존성 위반 (core:domain에 @Entity)
  - 불변 규칙 위반 (setter 존재)
  - 예외 계층 위반 (RuntimeException 직접 상속)
  - 트랜잭션 규칙 위반 (@Component에 @Transactional)
```

---

## 6. 설계 트레이드오프

### 6-1. 파일 분산 vs 단일 파일

**현재 선택**: Phase별 파일 분산 (7개 파일)
**대안**: 단일 WORKFLOW.md에 전체 내용

| 항목 | 분산 | 단일 |
|---|---|---|
| 컨텍스트 사용량 | 낮음 (필요 Phase만 로드) | 높음 (항상 전체 로드) |
| 탐색 편의성 | 낮음 (어떤 파일을 읽어야 하는지 판단 필요) | 높음 (한 파일에 전체) |
| 유지보수 | 낮음 (파일 간 참조 관계 관리 필요) | 높음 (한 곳만 수정) |
| 세션 격리 후 복원 | 높음 (필요 파일만 지정해서 재로드) | 낮음 (전체를 다시 읽어야) |

컨텍스트 효율이 가장 중요한 제약이므로 **분산 구조** 채택.

### 6-2. 세션 격리 조건 — 복잡 작업만 적용하는 이유

단순 작업(파일 5개 미만)에 세션 격리를 강제하면:
- `/clear` → 스킬 재로드 → 구현계획 재확인 → 약 2~3분 오버헤드
- 절감되는 컨텍스트: 수십 줄 수준 (탐색이 깊지 않으므로)
- **결론**: 오버헤드 > 이득 → 연속 진행이 합리적

복잡 작업(파일 5개 이상, 탐색 깊음):
- 탐색 대화 이력: 수백 줄 이상 쌓임
- 세션 격리 후 복원 시 컨텍스트: 구현계획.md + 탐색결과.md ≈ 150줄
- **결론**: 이득 >> 오버헤드 → 격리 적용

### 6-3. Tier 3에서 코드 작성을 완전히 금지하는 이유

아키텍처 결정이 필요한 상황에서 Claude가 먼저 코드를 작성하면:
1. 잘못된 방향으로 구현이 진행됨
2. 개발자가 코드를 보고 "이 방향으로 가야겠다"고 인식 전환
3. 실제로는 더 나은 방향이 있었으나 기존 코드에 끌려가는 앵커링 효과 발생

Tier 3는 **"무엇을 만들지"를 결정하는 단계**이므로 ADR 작성 후 결정이 확정되기 전까지 코드 작성은 유해하다.

---

## 부록: 핵심 설계 원칙 요약

| 원칙 | 구현 방식 |
|---|---|
| 컨텍스트는 희소 자원 | Phase별 지연 로드, BC 섹션 단위 로드, 세션 격리 |
| 방향이 먼저, 구현은 나중 | Tier 판정 → 계획 승인 → 구현 순서 강제 |
| 아키텍처는 자동 검증 | Harness 체크리스트를 커밋 전 필수 관문으로 배치 |
| 실수는 앞단에서 잡는다 | 구현 체크리스트(Phase 4) + Harness(Phase 6) 이중 검증 |
| 병렬 가능한 것은 병렬로 | 테스트 Agent 병렬 실행 (Phase 5) |
| 세션 상태는 파일로 저장 | MEMORY.md + workspace/*.md |