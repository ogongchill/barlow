# Barlow — Claude 워크플로우 완전 가이드

> 팀원들이 `.claude/` 워크플로우 시스템 전체를 이해하기 위한 레퍼런스 문서.
> 이슈를 받았을 때부터 PR을 올릴 때까지 모든 경우의 수를 다룬다.

---

## 목차

1. [시스템 개요 — 왜 이 워크플로우가 존재하는가](#1-시스템-개요)
2. [Tier 판정 시스템](#2-tier-판정-시스템)
3. [커맨드 레퍼런스](#3-커맨드-레퍼런스)
4. [개발 파이프라인 전체 흐름 (Phase -1 ~ 8)](#4-개발-파이프라인-전체-흐름)
5. [아키텍처 규칙 — 절대 위반 금지](#5-아키텍처-규칙)
6. [도메인 코딩 규칙 (core:domain)](#6-도메인-코딩-규칙)
7. [서비스 코딩 규칙 (core:service)](#7-서비스-코딩-규칙)
8. [Presentation 규칙 (app:api)](#8-presentation-규칙)
9. [예외 처리 규칙](#9-예외-처리-규칙)
10. [테스트 전략](#10-테스트-전략)
11. [커밋 & 브랜치 & PR 규칙](#11-커밋--브랜치--pr-규칙)
12. [/harness-check — 최종 관문](#12-harness-check--최종-관문)
13. [아키텍처 재설계 파이프라인 (/arch-redesign)](#13-아키텍처-재설계-파이프라인)
14. [구현 계획 수립 파이프라인 (/arch-plan)](#14-구현-계획-수립-파이프라인)
15. [경우의 수 총정리 — 어떤 상황에서 무엇을 하는가](#15-경우의-수-총정리)
16. [자주 하는 실수 & 위반 패턴](#16-자주-하는-실수--위반-패턴)

---

## 1. 시스템 개요

### 핵심 철학

**"상세 구현 로직은 잘 모르더라도 비즈니스의 흐름은 이해 가능한 로직이어야 한다."**

Barlow의 `.claude/` 워크플로우는 Claude AI 에이전트가 이 프로젝트에서 작업할 때 지켜야 하는 규칙의 집합이다. 모든 개발 작업은 이 파이프라인을 통해 진행된다.

### 파일 구조

```
.claude/
├── commands/
│   ├── tier/
│   │   ├── dev-auto.md       — Tier 1 강제 자율 진행
│   │   ├── dev-plan.md       — Tier 2 계획 후 승인 강제
│   │   └── dev-analyze.md    — Tier 3 사전 스크리닝
│   ├── pipeline/
│   │   ├── arch-redesign.md  — 아키텍처 재설계 (Stage 0~6)
│   │   └── arch-plan.md      — 구현 계획 수립 (Step 0~3)
│   └── harness-check.md      — 코드 규칙 준수 검증
├── skills/
│   ├── workflow-guide/
│   │   ├── SKILL.md          — workflow-guide 스킬 진입점
│   │   └── WORKFLOW.md       — 개발 파이프라인 Phase -1~8 전체 정의
│   └── coding-rules/
│       ├── SKILL.md          — coding-rules 스킬 진입점
│       ├── ARCHITECTURE.md   — 모듈/레이어 의존성 규칙
│       ├── DOMAIN_RULES.md   — core:domain 코딩 규칙
│       ├── SERVICE_RULES.md  — core:service + app:api 규칙
│       ├── ERROR_HANDLING.md — 예외 계층 규칙
│       └── TESTING.md        — 테스트 분류 및 작성 기준
└── templates/
    ├── adr.md                — ADR 문서 템플릿
    └── arch-redesign-report.md — 재설계 보고서 템플릿
```

### 단계 명칭 구분 (혼동 주의)

| 명칭 | 사용 컨텍스트 | 범위 |
|---|---|---|
| **Phase N** | `WORKFLOW.md` 개발 파이프라인 | Phase -1 ~ Phase 8 |
| **Stage N** | `/arch-redesign` 커맨드 내부 | Stage 0 ~ Stage 6 |
| **Step N** | `/arch-plan` 커맨드 내부 | Step 0 ~ Step 3 |
| **Task N** | `/arch-plan`의 계획 결과물 | 개별 구현 작업 단위 |

---

## 2. Tier 판정 시스템

### 핵심 원칙

**모든 이슈는 코드를 작성하기 전에 반드시 Tier를 판정한다.** Tier에 따라 이후 전체 흐름이 달라진다.

**오분류 원칙: 항상 더 보수적인 Tier로.** Tier 1 vs 2 애매 → Tier 2, Tier 2 vs 3 애매 → Tier 3.

### 판정 기준 — 5가지 축

| 판단 축 | 안전 (낮은 Tier) | 위험 (높은 Tier) |
|---|---|---|
| 변경 방향 | 순수 추가 (새 코드만) | 기존 동작 수정·삭제·이동 |
| 아키텍처 영향 | 단일 BC 내부 | BC 경계 변경, 모듈 추가·제거 |
| 가역성 | 쉽게 롤백 가능 | DB 스키마, API 계약, 데이터 마이그레이션 |
| 요구사항 완결성 | 명확한 스펙 | 탐색적 ("어떻게 해야 할까?") |
| 결정 주체 | 구현 방법 결정 | 기술 방향 결정 (trade-off) |

### Tier 1 — 자율 진행 (Autonomous)

**5가지 조건 ALL 충족 시에만 Tier 1:**

- [ ] 단일 BC만 영향
- [ ] 기존 API 계약 변경 없음 (엔드포인트 추가만 허용)
- [ ] DB 스키마 변경 없거나 순수 컬럼 추가만
- [ ] 이슈에 명확한 스펙 존재 (기대 동작, 제약 조건)
- [ ] 예상 변경 파일 ≤ 12개

**진행 방식**: Phase 0부터 사용자 승인 없이 Phase 8까지 자율 진행.

**주의**: "자율"은 Claude가 구현 결정을 스스로 내린다는 뜻. 이슈 전달은 여전히 개발자가 수동으로 한다.

**Tier 1 신호 (이슈 본문)**: `[FIX]`, `[FEAT]`, "기대 동작:", "재현 방법:", 특정 클래스 1~2개 언급

### Tier 2 — 계획 후 승인 (Plan & Approve)

**다음 중 하나라도 해당 시:**

- 영향 BC 2개 이상
- 기존 동작 변경 (순수 추가 아님)
- API 계약 변경 (Request/Response 구조 변경)
- 예상 변경 파일 > 12개
- 요구사항 일부 모호
- 단순 리팩토링 (범위 명확한 코드 구조 개선)

**진행 방식 — 두 가지 케이스:**

**일반 Tier 2**: Phase 0~3 실행 후 `.claude/workspace/구현계획.md` 작성 → 사용자 승인 대기 → 승인 후 Phase 4~8 진행.

**복잡 Tier 2** (코드베이스 깊은 분해가 필요한 경우):
→ `/project:pipeline:arch-plan {topic}` 실행 → `docs/plans/{날짜}-{topic}.md` 저장
→ 사용자 "확정" 후 **이 파이프라인 Phase 4** 로 복귀하여 진행.

**Tier 2 신호 (이슈 본문)**: "개선", "정리", "최적화", 여러 파일 동시 언급, 범위 명확한 "리팩토링"

### Tier 3 — 분석만 (Analyze Only)

**다음 중 하나라도 해당 시:**

- 아키텍처 재설계 (모듈 구조, BC 경계 재정의)
- 기술 trade-off 결정 필요 (기술 스택, 성능 vs 복잡성)
- 대규모 리팩토링 (다수 모듈 동시 영향)
- 레거시 청산 / 기술 부채 해소 (마이그레이션 계획 필요)
- ADR 작성이 선행되어야 하는 사안
- 보안 민감 변경 (인증, 권한, 암호화)
- 이슈 본문에 "재설계", "검토 필요", "결정 필요", "trade-off", "어떻게 해야" 포함

**진행 방식**: **코드 작성 일체 금지.**
→ `/project:tier:dev-analyze` 실행 → 분석 초안 도출 → 사용자 확인
→ 확인 후 `/project:pipeline:arch-redesign` 실행 → ADR 저장
→ ADR 승인 후 구현이 필요하면: `/project:pipeline:arch-plan {topic} adr={ADR경로}` 실행

**Tier 3 신호 (이슈 본문)**: "재설계", "마이그레이션", "전략", "trade-off", "ADR", "레거시", "기술 부채", "어떻게 할지", 다수 모듈 동시 언급

### Tier 판정 결과 출력 형식

이슈를 받으면 Claude는 반드시 다음 형식으로 Tier 판정 결과를 채팅에 출력한다:

```
## 이슈 Tier 판정

- 이슈: #{번호} {제목}
- 판정: Tier {1/2/3}
- 근거: {판정 이유 2~3줄}
- 진행 방식: {Autonomous 자율 진행 / 계획 후 승인 대기 / 분석 보고서만}

[Tier 3인 경우]
→ 코드 작성을 진행하지 않습니다.
  방향 결정 후 구체적인 구현 이슈를 별도로 만들어 주세요.
```

---

## 3. 커맨드 레퍼런스

### Tier 강제 지정 커맨드

| 커맨드 | 동작 | 언제 쓰는가 |
|---|---|---|
| `/project:tier:dev-auto` | Tier 1 강제 자율 진행 | 명확한 소규모 이슈인데 Tier 판정을 건너뛰고 싶을 때 |
| `/project:tier:dev-plan` | Tier 2 계획 후 승인 강제 | 계획을 먼저 보고 싶을 때 |
| `/project:tier:dev-analyze` | Tier 3 사전 스크리닝 | 재설계 논의 전 초안이 필요할 때 |

#### `/project:tier:dev-auto` 안전 거부 조건

다음 중 하나라도 해당하면 실행을 **거부**한다:
- DB 스키마 변경 (컬럼 추가 제외한 구조 변경)
- 기존 API 계약 변경 (엔드포인트 삭제, Request/Response 필드 변경)
- 보안 관련 변경 (인증, 권한, 암호화)

거부되지 않으면 채팅에 다음을 출력 후 즉시 진행:
```
[/project:dev-auto] Tier 1 자율 진행합니다. (사용자 승인 없이 Phase 8까지 완료)
이슈: {이슈 제목 또는 요구사항 요약}
```

### 파이프라인 커맨드

| 커맨드 | 동작 | 산출물 |
|---|---|---|
| `/project:pipeline:arch-redesign` | 아키텍처 재설계 분석 + ADR 생성 (Stage 0~6) | `docs/adr/ARCH-{NNN}-{날짜}-{topic}.md` |
| `/project:pipeline:arch-plan {topic}` | 독립 구현 계획 수립 (Step 0~3) | `docs/plans/{날짜}-{topic}.md` |
| `/project:pipeline:arch-plan {topic} adr={경로}` | ADR 기반 구현 계획 수립 | `docs/plans/{날짜}-{topic}.md` |

### 검증 커맨드

| 커맨드 | 동작 | 언제 실행 |
|---|---|---|
| `/project:harness-check` | Barlow 코딩 규칙 준수 검증 | Phase 6 최종 관문 (커밋 전 필수) |
| `/project:harness-check {파일경로}` | 특정 파일/모듈만 검증 | 독립 실행 시 |

---

## 4. 개발 파이프라인 전체 흐름

### 전체 흐름도

```
이슈/요구사항 접수
       |
   Phase -1: Tier 판정
       |
   +---+---+---+
   |       |   |
Tier 1  Tier 2  Tier 3
   |       |       |
   |    Phase 0~3  /dev-analyze
   |    구현계획.md   |
   |    승인 대기  /arch-redesign
   |       |       |
   |    승인 후   ADR 생성
   |       |    승인 대기
   |       |       |
   |    (복잡 Tier 2인 경우)
   |    /arch-plan 실행
   |    확정 대기
   |       |
Phase 0: 스킬 & 규칙 로드
       |
Phase 1: 요구사항 분석
       |
Phase 2: 코드베이스 탐색
       |
Phase 3: 구현 계획 (Tier 2: 승인 대기)
       |
Phase 4: 코드 구현
       |
Phase 5: 테스트 작성
       |
Phase 6: 린트 & 검증 (/harness-check)
       |
Phase 7: 커밋
       |
Phase 8: 브랜치 & PR
```

---

### Phase -1 — 이슈 트리어지 (Tier 판정)

**입력**: 이슈 번호, 이슈 본문, 요구사항
**출력**: Tier 판정 결과 채팅 출력

5가지 판정 축과 이슈 본문 신호를 기반으로 Tier를 결정한다. 상세 기준은 [§2 Tier 판정 시스템](#2-tier-판정-시스템) 참조.

---

### Phase 0 — 스킬 & 규칙 로드

**목적**: 작업에 필요한 모든 규칙 파일을 메모리에 로드한다.

**필수 실행 순서**:
1. `workflow-guide` 스킬 로드 (WORKFLOW.md)
2. `coding-rules` 스킬 로드
3. `docs/DOMAIN_ENCYCLOPEDIA.md` 읽기 — **예외 없음, 절대 생략 불가**
4. 작업 유형에 따라 추가 규칙 파일 로드:

| 작업 유형 | 추가 로드 파일 |
|---|---|
| 도메인 클래스(AR/VO/Port) 생성·수정 | `ARCHITECTURE.md` → `DOMAIN_RULES.md` |
| core:service 작성 | `ARCHITECTURE.md` → `SERVICE_RULES.md` |
| API 엔드포인트 작성 | `SERVICE_RULES.md` |
| 예외 추가·수정 | `ERROR_HANDLING.md` |
| 테스트 작성 | `TESTING.md` |
| 전체 기능 구현 | 모든 파일 |

---

### Phase 1 — 요구사항 분석

**목적**: 코드를 건드리기 전에 무엇을 어디에 만들지 정확히 결정한다.

**1-1. 도메인 용어 추출**
- 요구사항에서 도메인 명사 추출
- `docs/DOMAIN_ENCYCLOPEDIA.md`의 빠른 참조 테이블에서 정확한 코드 클래스명·패키지명 확인
- 모호한 용어 → Encyclopedia의 "혼동 주의" 섹션 대조

**1-2. 영향 BC 식별**
- 어떤 Bounded Context가 영향을 받는지 목록화
- BC 간 의존 관계 확인 (DOMAIN_ENCYCLOPEDIA.md 의존 관계 섹션)

**1-3. 구현 위치 결정**
각 구현 요소별 정확한 파일 경로를 사전에 결정:

```
[ ] core:domain/{bc}/        → 새 AR/VO/Repository/Command/Query?
[ ] core:service/{bc}/       → 새 Service/Reader/Manager?
[ ] infra:storage/           → 새 JpaEntity/Adapter?
[ ] app:api/controller/v1/   → 새 Controller/Request/Response?
[ ] 테스트 파일 위치?
```

**1-4. 복잡도 판단 — Phase 2/3 파일 작성 여부 결정**

복잡 조건 (하나라도 해당 시 복잡):
- 3개 이상 레이어 변경 (domain + service + api)
- 2개 이상 BC 영향
- 신규 AR 또는 Port Interface 설계 포함
- 예상 변경 파일 5개 이상

- **단순 작업**: Phase 2/3에서 채팅 출력만 (파일 작성 생략)
- **복잡 작업**: Phase 2에서 `탐색결과.md`, Phase 3에서 `구현계획.md` 작성

**1-5. 브랜치 전략 결정**

```
예상 파일 수 ≤ 12개
  → 단일 브랜치 전략

예상 파일 수 > 12개
  → 서브 브랜치 전략 (레이어별 분리)
```

---

### Phase 2 — 코드베이스 탐색

**목적**: 기존 코드의 패턴을 파악하고 참고할 구현체를 찾는다.

**탐색 항목**:
- 동일 BC의 기존 AR, Repository, Service 읽기
- 유사한 기능 구현체 패턴 파악 (참고할 코드 찾기)
- 기존 예외 클래스 확인 (신규 추가 필요 여부)
- 새 클래스가 사용할 기존 Port Interface 목록 확인
- build.gradle 의존성이 이미 선언되어 있는지 확인

**복잡 작업 시 → `.claude/workspace/탐색결과.md` 작성** (매 작업마다 덮어씀):

```markdown
# 탐색 결과

## 요구사항 요약
{한 줄 요약}

## 영향 BC 및 파일 목록

### {BC명}
- `{파일경로}`: {현재 역할 / 변경이 필요한 이유}

## 재사용 가능한 기존 패턴
- {패턴 설명}: `{참조 파일경로}`

## 신규 생성 필요 항목
- [ ] `{파일경로}`: {역할 한 줄}

## 주의사항 / 의존성
- {빌드 의존성, 순환 참조 위험 등}
```

---

### Phase 3 — 구현 계획

**목적**: 코드를 짜기 전에 전체 변경 범위와 순서를 확정한다.

**Tier 1 (단순 작업)**: 채팅에 계획 출력 후 즉시 진행.

**Tier 2 (복잡 작업)**: `.claude/workspace/구현계획.md` 작성 후 반드시 사용자 승인 대기.

구현계획.md 형식:

```markdown
# 구현 계획

## 요구사항 요약

## 변경 범위
- [ ] `{파일경로}`: {변경 내용}

## 구현 순서
1. core:domain
2. core:service
3. infra:storage
4. app:api
5. 테스트

## 핵심 설계 결정

## 아키텍처 검증
- [ ] core:domain — 순수 POJO 유지
- [ ] core:service — infra:* 직접 참조 없음
- [ ] 불변 객체 패턴 준수
- [ ] 예외 계층 규칙 준수

## 테스트 계획

## 커밋 계획

## 브랜치 전략
```

**Tier 2 승인 없이 코드 작성 절대 금지.**

---

### Phase 4 — 코드 구현

**의존성 방향에 따른 구현 순서 (반드시 준수):**

```
1. core:domain  — AR/VO/Command/Query/Repository Interface/Exception
2. core:service — Implement Layer (Reader/Manager/Activator 등)
3. core:service — Business Layer (Service/Facade)
4. infra:storage — JpaEntity/JpaRepository/RepositoryAdapter
5. app:api      — Controller/Request/Response
```

**각 파일 작성 후 체크리스트 → 상세 내용은 §5~§9 참조**

---

### Phase 5 — 테스트 작성

**작성 순서:**

```
1. 도메인 정책 단위 테스트 (태그 없음) — 비즈니스 규칙 경계값 중심
2. Service 통합 테스트 (@Tag("develop"))
3. Repository 테스트 (@Tag("context")) — 필요 시
```

**상세 기준 → §10 테스트 전략 참조**

---

### Phase 6 — 린트 & 검증

**실행 순서:**

```bash
# 1. 린트 적용
./gradlew spotlessApply

# 2. 단위 테스트
./gradlew unitTest

# 3. 통합 테스트 (변경 범위에 따라 선택)
./gradlew developTest
./gradlew contextTest

# 4. 컴파일 확인
./gradlew :core:domain:compileJava
./gradlew :core:service:compileJava
./gradlew :app:api:compileJava

# 5. Harness 검증 (최종 관문 — 반드시 실행)
/project:harness-check
```

**테스트 실패 시**: 원인 분석 후 코드 수정. 같은 실패를 반복 재시도하지 않는다.

**Harness 검증 FAIL 시**: FAIL 항목이 1개라도 있으면 커밋하지 않고 수정 후 재검증.

---

### Phase 7 — 커밋

**커밋 단위 원칙**: 기능/목적 단위로 분리. 하나의 커밋 = 하나의 변경 이유.

**커밋 순서 (전체 기능 구현 시):**

```bash
# 1. 도메인 레이어
git add core/domain/...
git commit -m "feat : {BC명} {기능} 도메인 모델 추가"

# 2. 서비스 레이어
git add core/service/...
git commit -m "feat : {BC명} {기능} 서비스 레이어 구현"

# 3. 영속성 레이어
git add infra/storage/...
git commit -m "feat : {BC명} {기능} JPA 어댑터 구현"

# 4. API 레이어
git add app/api/...
git commit -m "feat : {BC명} {기능} API 엔드포인트 추가"

# 5. 테스트
git add **/test/...
git commit -m "test : {BC명} {기능} 테스트 추가"

# 6. 린트 (항상 마지막)
./gradlew spotlessApply
git add -A
git commit -m "lint : lint 적용"
```

**커밋 형식**: `{type} : {한국어 설명}` — space-colon-space 필수

**타입 목록:**

| 타입 | 사용 시점 |
|---|---|
| `feat` | 새 기능 추가 |
| `fix` | 버그 수정 |
| `refactor` | 리팩토링 (기능 변화 없음) |
| `rename` | 클래스/파일명 변경 |
| `lint` | 포매터 적용 |
| `gradle` | 빌드 설정 변경 |
| `batch` | 배치 관련 변경 |
| `chore` | 기타 설정, 빌드 외 잡무 |
| `test` | 테스트 추가/수정 |
| `docs` | 문서 변경 |

---

### Phase 8 — 브랜치 & PR

**브랜치 네이밍:**

```
# 단일 브랜치
feat/issue/{이슈번호}
fix/issue/{이슈번호}
refactor/{설명 또는 #번호}
chore/{설명}
docs/{설명}

# 서브 브랜치 (파일 > 12개 시)
feat/issue/{이슈번호}/domain
feat/issue/{이슈번호}/service
feat/issue/{이슈번호}/storage
feat/issue/{이슈번호}/api
```

**모든 브랜치는 `develop`에서 분기한다.**

**PR 크기 기준:**

| 기준 | 권장 | 최대 |
|---|---|---|
| 변경 파일 수 | ≤ 8개 | 12개 |
| 라인 추가(+) | ≤ 400줄 | 600줄 |

**레이어 혼재 규칙:**
- 허용: 인접 레이어 동시 변경 (domain + service, service + api)
- 금지: 2단계 건너뛰기 (domain + api 동시), domain + infra:storage 동시

**단일 브랜치 PR:**

```bash
git checkout develop && git pull
git checkout -b feat/issue/{번호}
# ... 구현 & 커밋 ...
gh pr create \
  --base develop \
  --title "{type}: {한국어 설명}" \
  --body "$(cat .github/PULL_REQUEST_TEMPLATE.md)"
```

**서브 브랜치 PR 흐름:**

```
develop
  └── feat/issue/{번호}          ← 빈 베이스 브랜치
        ├── feat/issue/{번호}/domain   → PR → feat/issue/{번호}  [1]
        ├── feat/issue/{번호}/service  → PR → feat/issue/{번호}  [2] domain 머지 후
        ├── feat/issue/{번호}/storage  → PR → feat/issue/{번호}  [3] service 머지 후
        └── feat/issue/{번호}/api      → PR → feat/issue/{번호}  [4] storage 머지 후
      feat/issue/{번호}          → PR → develop                  [5] 전체 완료 후
```

**규칙**: 이전 서브 PR이 베이스 브랜치에 머지되기 전까지 다음 서브 PR을 오픈하지 않는다.

---

## 5. 아키텍처 규칙

### 레이어 구조

```
[Presentation]   app:api / app:batch
                 Controller, Request/Response
      ↓
[Business]       core:service  (business/ 패키지)
                 Service, Facade
      ↓
[Implement]      core:service  (impl/ 패키지)
                 Reader, Manager, Creator, Appender
      ↓
[Data Access]    infra:*
                 기술 의존성 격리, Port Interface 구현

[Shared Models]  core:domain
                 AR, VO, Enum, Port Interface (Repository)
```

### 의존성 방향 (절대 위반 금지)

```
app:api / app:batch
    ↓  implementation
core:service
    ↓  implementation
core:domain  ←  implementation  ←  infra:*
```

- `infra:*`는 `core:domain`의 Port Interface를 구현한다.
- `app:api`는 `infra:storage`를 `runtimeOnly`로만 의존 — 컴파일 타임에 JPA 기술에 접근 불가.

### 4대 규칙

**규칙 1 — 순방향 참조만 허용 (위 → 아래)**

**규칙 2 — 역방향 참조 금지**
Implement Layer의 Reader가 Business Layer의 Service를 알면 안 된다.

**규칙 3 — 레이어 건너뜀 금지**
Business Layer(Service)가 JPA Repository, JPA Entity를 직접 참조하면 안 된다.

```java
// BAD — Business가 Data Access 직접 참조
@Service
public class AccountCreateService {
    private final UserJpaRepository userJpaRepository; // 금지!
}

// GOOD — Business는 Implement Layer만 안다
@Service
public class AccountCreateService {
    private final UserCreator userCreator;
    private final TermManager termManager;
}
```

**규칙 4 — 동일 레이어 간 참조 금지. 단, Implement Layer는 예외**

```java
// GOOD — Implement 간 협력 허용
@Component
public class LegislationAccountReader {
    private final LegislationAccountRepository legislationAccountRepository;
    private final SubscriptionReader subscriptionReader;         // OK
    private final NotificationSettingReader notificationSettingReader; // OK
}

// BAD — Business 간 직접 참조 금지
@Service
public class AccountCreateService {
    private final MemberRegisterService memberRegisterService; // 금지!
}
```

### 모듈 확장 원칙

**새 요구사항 → 새 모듈 추가. 기존 모듈에 기술 추가 금지.**

```
// 예: 새 외부 API 연동 요구사항
infra/
└── new-external-api/   ← 새 모듈. 기존 모듈에 추가 금지.
```

의존성 선언은 `implementation`을 최우선 사용 (`api` 키워드 사용 금지).

### 위반 사례 요약

| 위반 패턴 | 올바른 방향 |
|---|---|
| Business Layer에서 JPA Repository 직접 주입 | Implement Layer(Reader/Manager) 경유 |
| Business Layer에서 `infra:*` 클래스 직접 import | Port Interface만 알아야 함 |
| `app:api` Controller에서 `infra:*` 클래스 직접 import | runtimeOnly — 컴파일 타임 접근 불가 |
| 동일 레이어 Business Service 간 직접 주입 | Facade로 상위에서 조합 |
| 새 기술 기능을 기존 infra 모듈에 추가 | 개념 단위 새 infra 모듈 추가 |
| 모듈 의존성을 `api` 키워드로 선언 | `implementation`으로 기술 캡슐화 |

---

## 6. 도메인 코딩 규칙

### 도메인 철학

- `core:domain`은 Spring, JPA 등 어떤 인프라 프레임워크도 참조하지 않는다.
- 비즈니스 규칙은 Service가 아닌 **Domain 객체 내부**에 캡슐화한다 (Rich Domain Model).
- Domain 객체는 불변이다. 상태 변경은 항상 새 인스턴스를 반환한다.

### 도메인 클래스 타입 선택 규칙

**판단 흐름:**

```
저장소에서 identity로 조회·저장되는가?
  ├── YES → Aggregate Root / Entity → class
  └── NO  → 데이터 전달 용도인가?
              ├── YES, 행위 없음 → record (VO/Read Model/Query/Command)
              └── NO, 비즈니스 계산 포함 → final class (VO with behavior)
```

| DDD 구성 요소 | Java 타입 | 이유 |
|---|---|---|
| **Aggregate Root** | `class` | identity 기반 동등성 + 상태 전환 행위. `final`/`record` 금지 |
| **Entity (비루트)** | `class` | AR 경계 내에서 AR에 의해 제어되는 식별자 보유 객체 |
| **Value Object (행위 있는 불변)** | `final class` | 값 기반 동등성 + 비즈니스 계산 |
| **Value Object (순수 데이터 홀더)** | `record` | 행위 없는 단순 구조체 |
| **Read Model** | `record` | 조회 전용 데이터 집계. 불변 보장 |
| **Query / Command Object** | `record` | 입력 파라미터 객체. 불변 보장 |
| **Domain Policy** | `class` | 정책 상태 보유 + 검증 행위 |
| **Port Interface** | `interface` | 구현 기술을 모르는 포트 계약 |

**주의: `record`에 도메인 행위를 넣지 않는다.**

```java
// BAD — record에 도메인 행위
public record UserExternalAuth(Long userNo, List<ExternalPrincipal> principals) {
    public boolean has(AuthProvider authProvider) { ... }  // 행위가 있으면 class로!
}

// GOOD — 행위 있으므로 class
public class UserExternalAuth {
    private final Long userNo;
    public boolean has(AuthProvider authProvider) { ... }
}
```

### 불변 객체 패턴 3가지

**with 패턴** — 컨텍스트 데이터 주입 (조회 후 사용자 컨텍스트 채울 때):

```java
public LegislationAccount withSubscribed(boolean subscribed) {
    return new LegislationAccount(no, type, description, postCount, subscriberCount, subscribed, isNotifiable);
}

// 사용
LegislationAccount enriched = account
    .withSubscribed(true)
    .withNotifiable(false);
```

**activate/deactivate 패턴** — 이진 상태 전환:

```java
public Subscription activate() {
    return new Subscription(subscriberNo, info.subscribeAccountNo(), info.subscribeAccountType(), true);
}
public Subscription deactivate() {
    if (!this.isActive()) {
        throw SubscriptionDomainException.alreadyUnSubscribed(getLegislationType());
    }
    return new Subscription(subscriberNo, info.subscribeAccountNo(), info.subscribeAccountType(), false);
}
```

**modify* 패턴** — 특정 필드 갱신:

```java
public Device modifyToken(String newToken) {
    return new Device(userNo, deviceId, deviceOs, newToken, status);
}
```

**공통 원칙: setter를 전혀 사용하지 않는다.**

### Static Factory Method 패턴

생성자는 `private` 또는 `package-private`. 외부에서는 의미 있는 이름의 정적 팩토리만 사용.

```java
// GOOD
User.of(userNo, role)
Subscription.activate(subscriber, subscriptionInfo)
AccountDomainException.notFound()

// BAD
new User(...)
new AccountDomainException("계정이 없음")
```

### Rich Domain Model

비즈니스 규칙은 Service가 아닌 Domain 객체 내부에 위치한다.

```java
// BAD — 비즈니스 규칙이 Service에 있음 (Anemic Domain)
@Service
public class SubscriptionService {
    public void deactivate(Subscription subscription) {
        if (subscription.getStatus() == INACTIVE) {
            throw new IllegalStateException("이미 구독 취소 상태"); // 금지!
        }
        subscription.setStatus(INACTIVE); // setter 금지!
    }
}

// GOOD — 비즈니스 규칙이 Domain 객체 내부에 있음
public class Subscription {
    public Subscription deactivate() {
        if (!this.isActive()) {
            throw SubscriptionDomainException.alreadyUnSubscribed(getLegislationType());
        }
        return new Subscription(subscriberNo, info.subscribeAccountNo(), info.subscribeAccountType(), false);
    }
}
```

### Port Interface 정의

외부 의존성의 인터페이스는 반드시 `core:domain`에 정의. 구현체(Adapter)는 `infra:*`에 위치.

```java
// core:domain에 정의
public interface UserRepository {
    User retrieve(UserQuery query);
    User create(UserRegisterCommand command);
}

// infra:storage가 구현
@Component
public class UserRepositoryAdapter implements UserRepository { ... }
```

### 패키지 구성 원칙

**패키지 = Aggregate 단위** (UseCase/기능 단위 패키지 금지)

```
core:domain/
├── (루트)                  ← User, Passport (전역 공유 프리미티브)
├── account/                ← User Aggregate
│   └── term/               ← 도메인 개념 단위 서브패키지 허용
├── billpost/
├── device/
├── externalauth/
├── legislationaccount/
├── notificationcenter/
├── notificationsetting/
├── reaction/
├── subscribe/              ← 패키지명 주의: subscription 아님
└── version/
```

서브패키지 허용 기준:
- 허용: 도메인 개념 단위 (`account/term/`)
- 금지: UseCase 흐름 단위 (`account/create/`, `account/login/`)
- 금지: 화면 단위 (`home/`)

### AR 간 참조 — ID-only

AR 간 객체 직접 참조 금지. `long` ID로만 참조.

```java
// GOOD
public class Subscription {
    private final long subscriberNo;  // User 객체 아님, userNo만
}

// BAD
public class Subscription {
    private final User subscriber;  // 직접 참조 금지!
}
```

### CQRS — Read Model 위치 결정

| 유형 | 위치 |
|---|---|
| 단일 Aggregate 투영 | `core:domain` 해당 Aggregate 패키지 |
| 크로스 Aggregate 조합 | `core:service` 해당 feature 패키지 |

### Command/Query 소속 원칙

**모든 Command/Query는 `core:domain`에 정의한다.**
(core:service에 두면 core:domain이 역방향 참조해야 함)

### infra:storage 변환 규칙

```java
// core:domain → infra:storage
XxxJpaEntity.from(domainObject)

// infra:storage → core:domain
entity.toDomain()
```

네이밍 규칙:
- Port Interface: `UserRepository`
- Adapter 구현체: `UserRepositoryAdapter`
- Spring Data JPA: `UserRepositoryJpaRepository`
- JPA Entity: `UserJpaEntity`

---

## 7. 서비스 코딩 규칙

### core:service 레이어 구조

```
core:service/
├── {bc}/
│   ├── business/          ← @Service — 비즈니스 흐름 중계
│   │   ├── {명사}{동사}Service.java
│   │   └── {명사}Facade.java
│   └── impl/              ← @Component — 상세 구현 도구
│       ├── {명사}Reader.java
│       ├── {명사}Appender.java
│       ├── {명사}Creator.java
│       └── {명사}Manager.java
```

`core:domain`은 Aggregate 단위만 허용.
`core:service`는 feature 단위도 허용 (`home/`, `menu/` 등).

### Business Layer 규칙 (@Service)

- 모든 public 메서드에 `@Transactional` 필수
- 읽기 전용 메서드는 `@Transactional(readOnly = true)`
- **Implement Layer 클래스(Reader, Manager 등)만 주입받는다. `infra:*` 직접 주입 금지.**
- Business Layer 간 직접 주입 금지 → Facade로 상위에서 조합

```java
// GOOD — 비즈니스 흐름이 코드로 읽힌다
@Service
public class AccountCreateService {
    private final UserCreator userCreator;
    private final TermManager termManager;
    private final NotificationSettingActivator notificationSettingActivator;

    @Transactional
    public User createGuest(UserCreateCommand command, List<TermAgreement> agreements) {
        termManager.validateAgreements(agreements);
        User user = userCreator.create(command);
        termManager.saveAgreements(agreements, user);
        notificationSettingActivator.activateDefault(user);
        return user;
    }
}
```

### Facade — 여러 Service 조합

```java
@Service
public class HomeRetrieveFacade {
    private final MyHomeInfoRetrieveService myHomeInfoRetrieveService;
    private final TodayBillPostThumbnailRetrieveService todayBillPostThumbnailRetrieveService;

    @Transactional(readOnly = true)
    public MyHomeStatus retrieveHome(User user) {
        List<MyLegislationAccount> accounts = myHomeInfoRetrieveService.retrieve(user);
        boolean isNotificationArrived = notificationCenterItemRetrieveService.hasUnread(user);
        return new MyHomeStatus(accounts, isNotificationArrived);
    }
}
```

### Implement Layer 규칙 (@Component)

- Implement Layer 클래스끼리는 서로 협력 가능
- Port Interface(`core:domain`의 Repository)를 통해서만 `infra:*`와 통신
- JPA Repository, FCM SDK 등 기술 클래스 직접 사용 금지
- **`@Transactional` 없음** — 트랜잭션 관리는 Business Layer에서

### Implement Layer 네이밍 컨벤션

| 역할 | 접미사 | 예시 |
|---|---|---|
| 조회 | `Reader` | `LegislationAccountReader`, `BillPostReader` |
| 저장/추가 | `Appender` | `NotificationCenterAppender` |
| 생성 | `Creator` | `UserCreator` |
| 관리 (조회+변경 조합) | `Manager` | `TermManager` |
| 활성화/비활성화 처리 | `Activator` | `NotificationSettingActivator`, `SubscriptionActivator` |
| 특정 이벤트 처리 | `Handler` | `LegislationAccountWithdrawalHandler` |
| 처리 실행 | `Processor` | `ReactionProcessor` |
| 오케스트레이터 | `Orchestrator` | `UserWithdrawalOrchestrator` |

---

## 8. Presentation 규칙

### Request 검증 — Validatable

Request 클래스는 `Validatable`을 구현하여 Controller 진입 시점에 즉시 검증한다.

```java
public record SignupRequest(
    String deviceOs,
    String deviceId,
    Map<Long, Boolean> termAgreements
) implements Validatable {

    @Override
    public void validate() {
        if (deviceId == null || deviceId.isBlank()) {
            throw CoreApiException.badRequest("deviceId는 필수입니다.");
        }
    }
}

@PostMapping("/guest/signup")
public ApiResponse<LoginResponse> guestSignup(@RequestBody SignupRequest request) {
    request.validate(); // 진입 시점 즉시 검증
    // ...
}
```

### Request → Domain Command 변환

Controller는 Request를 Domain Command/Query로 변환하여 Service에 전달한다. Service에 Request 직접 전달 금지.

```java
// GOOD
User guest = accountCreateService.createGuest(
    request.toGuestCommand(),
    request.toTermAgreements(LocalDateTime.now())
);

// BAD
accountCreateService.createGuest(request); // Service가 Request를 알면 안 됨!
```

### Passport — Controller 파라미터

인증된 사용자 컨텍스트는 `Passport`로 Controller에 전달된다.

```java
@GetMapping("/me")
public ApiResponse<MyAccountResponse> getMyAccount(Passport passport) {
    User user = passport.getUser();
    // ...
}
```

---

## 9. 예외 처리 규칙

### 예외 계층 구조

```
[Domain Layer]   CoreDomainException   — 순수 Java, HTTP/Spring 의존 없음
[Auth Layer]     CoreAuthException     — 인증 서비스(OIDC, JWT) 전용
[API Layer]      CoreApiException      — Controller / Service / Filter
```

### CoreDomainException — 도메인 계층

**반드시 BC별 하위 예외 클래스 + static factory 패턴으로 사용.**

```java
// GOOD — BC별 예외 클래스 + static factory
public class SubscriptionDomainException extends CoreDomainException {

    private SubscriptionDomainException(CoreDomainExceptionType type, String message) {
        super(type, message);
    }

    public static SubscriptionDomainException alreadySubscribed(LegislationType type) {
        return new SubscriptionDomainException(
            CoreDomainExceptionType.CONFLICT_EXCEPTION,
            "이미 구독중인 입법계정 " + type + " 입니다");
    }
}

// BAD — 표준 예외를 비즈니스 규칙 위반에 사용
throw new IllegalStateException("이미 구독 취소 상태"); // 금지!

// BAD — CoreDomainException 직접 throw
throw new CoreDomainException(...); // BC별 하위 클래스 사용!
```

**프로그래밍 계약 위반(precondition check)에는 Java 표준 예외 허용:**

```java
public SemanticVersion(int major, int minor, int patch) {
    if (major < 0) {
        throw new IllegalArgumentException("major는 0 이상이어야 합니다"); // OK
    }
}
```

### BC별 도메인 예외 목록

| 예외 클래스 | BC |
|---|---|
| `AccountDomainException` | account |
| `RegistrationException` | account (회원가입/외부인증) |
| `BillPostDomainException` | billpost |
| `ReactionDomainException` | reaction |
| `NotificationSettingDomainException` | notificationsetting |
| `SubscriptionDomainException` | subscribe |
| `ClientVersionException` | version |

### CoreApiException — API 계층

```java
// GOOD
throw new CoreApiException(CoreApiErrorType.NOT_FOUND);
throw CoreApiException.badRequest("deviceId는 필수입니다.");

// BAD — 도메인 예외를 API 레이어에서 throw
throw new AccountDomainException(...); // 금지!
```

### 로깅 & 알림 전략

**CoreDomainExceptionLevel:**

| Level | 의미 | 로그 레벨 | Alert |
|---|---|---|---|
| `BUSINESS` | 사용자 행위에 의한 정상 거절 (중복 구독, 이미 취소 등) | WARN | 없음 |
| `IMPLEMENTATION` | 발생해서는 안 되는 버그 또는 데이터 불일치 | ERROR | 즉각 알림 발송 |

```java
// BUSINESS — 정상적 비즈니스 거절 → WARN
public static SubscriptionDomainException alreadySubscribed(LegislationType type) {
    return new SubscriptionDomainException(
        CoreDomainExceptionCode.E409, CoreDomainExceptionLevel.BUSINESS, ...);
}

// IMPLEMENTATION — 있어야 할 데이터가 없음 (버그 가능성) → ERROR + Alert
public static BillPostDomainException notFound(String billId) {
    return new BillPostDomainException(
        CoreDomainExceptionCode.E500, CoreDomainExceptionLevel.IMPLEMENTATION, ...);
}
```

### 응답 포맷

**성공:**
```json
{ "result": "SUCCESS", "data": { ... }, "error": null }
```

**실패:**
```json
{
  "result": "ERROR",
  "data": null,
  "error": {
    "code": "E404",
    "message": "The requested resource could not be found.",
    "data": null
  }
}
```

- `error.message`는 클라이언트에게 노출되는 범용 고정 메시지 (구현 세부 정보 담지 않음)
- 클라이언트는 `error.code`를 기반으로 프론트엔드 로직을 분기

### 새 예외 추가 가이드

**Case A: 새 BC 도메인 예외 추가:**

```java
package com.barlow.core.domain.{bc};

public class {BC명}DomainException extends CoreDomainException {

    private {BC명}DomainException(CoreDomainExceptionCode code, CoreDomainExceptionLevel level, String message) {
        super(code, level, message);
    }

    public static {BC명}DomainException notFound(String id) {
        return new {BC명}DomainException(
            CoreDomainExceptionCode.E500,
            CoreDomainExceptionLevel.IMPLEMENTATION,
            "{대상}을 찾을 수 없습니다: id=" + id);
    }
}
```

ControllerAdvice 변경 불필요 — CoreDomainException 핸들러가 자동 처리.

**Case B: 새 API 에러 타입 추가:**

```java
// 1. CoreApiErrorCode에 새 코드 추가
// 2. CoreApiErrorType에 새 타입 추가
UNPROCESSABLE(HttpStatus.UNPROCESSABLE_ENTITY, E422, "Request could not be processed.", WARN)
// 3. CoreApiException에 static factory 추가 (선택)
```

---

## 10. 테스트 전략

### 테스트 분류 체계

| 태그 | 실행 명령 | 사용 시점 |
|---|---|---|
| 태그 없음 | `./gradlew unitTest` | 순수 Java 단위 테스트. Spring 컨텍스트 없음 |
| `@Tag("develop")` | `./gradlew developTest` | Spring 컨텍스트 포함 통합 테스트 |
| `@Tag("context")` | `./gradlew contextTest` | DB 컨텍스트 포함 Repository 테스트 |
| `@Tag("restdocs")` | `./gradlew restDocsTest` | REST Docs API 문서 생성 테스트 |

### 도메인 정책 테스트 (태그 없음)

- Spring 컨텍스트 없이 순수 Java로 작성
- 실제 객체(Real Collaborator) 사용. Mock 최소화.
- 경계값(boundary value) 중심 테스트

```java
class TermsPolicyTest {

    @Test
    @DisplayName("필수 약관에 동의하지 않으면 RegistrationException이 발생한다.")
    void validate_RequiredTermNotAgreed_ThrowsRegistrationException() {
        // given
        TermsPolicy policy = TermsPolicy.from(activeTerms);
        List<TermAgreement> agreements = List.of(
            TermAgreement.disagreedAt(1L, LocalDateTime.now())  // 필수 약관 미동의
        );

        // when & then
        assertThatThrownBy(() -> policy.validate(agreements))
            .isInstanceOf(RegistrationException.class);
    }
}
```

### 통합 테스트 (@Tag("develop"))

- `DevelopTest` 상속
- 실제 Spring Bean 의존성 연결 검증

```java
class AccountCreateServiceTest extends DevelopTest {

    @Autowired
    private AccountCreateService accountCreateService;

    @Test
    @DisplayName("게스트 계정 생성 시 기본 알림 설정이 활성화된다.")
    void createGuest_Success_ActivatesDefaultNotificationSettings() { ... }
}
```

### Repository 테스트 (@Tag("context"))

- JPA Repository Adapter가 실제 DB (H2)와 올바르게 동작하는지 검증

```java
@Tag("context")
class SubscriptionRepositoryAdapterTest { ... }
```

### 테스트 구조 규칙

**메서드 네이밍: `테스트대상_상태_기대결과`**

```java
// GOOD
void validate_RequiredTermNotAgreed_ThrowsRegistrationException()
void activate_AlreadyActive_ThrowsSubscriptionDomainException()
void deactivate_InactiveSubscription_ReturnsDeactivatedSubscription()

// BAD
void testValidate()     // 정보 없음
void 약관검증_성공()     // 영문 메서드명 규칙 불일치
```

**@DisplayName: 완전한 한글 문장 ("~할 때, ~하면, ~한다/된다")**

```java
// GOOD
@DisplayName("필수 약관에 동의하지 않으면 RegistrationException이 발생한다.")
@DisplayName("이미 구독 취소한 입법계정을 다시 취소하면 SubscriptionDomainException이 발생한다.")

// BAD
@DisplayName("구독 테스트")     // 너무 추상적
@DisplayName("subscription test") // 영문 + 불완전
```

**@Nested: Describe-Context-It (BDD 스타일)**

```java
class SubscriptionTest {

    @Nested
    @DisplayName("deactivate — 구독 취소")
    class Deactivate {

        @Nested
        @DisplayName("구독 활성 상태일 때")
        class WhenActive {

            @Test
            @DisplayName("구독을 취소하면 isActive()가 false가 된다.")
            void deactivate_ActiveSubscription_ReturnsFalseIsActive() { ... }
        }

        @Nested
        @DisplayName("이미 구독 취소 상태일 때")
        class WhenAlreadyInactive {

            @Test
            @DisplayName("구독을 취소하면 SubscriptionDomainException이 발생한다.")
            void deactivate_InactiveSubscription_ThrowsSubscriptionDomainException() { ... }
        }
    }
}
```

**given / when / then 주석 구분 필수**

### 테스트 대역 사용 기준

| 상황 | 대역 종류 |
|---|---|
| 외부 서비스 (FCM, 외부 API) | **Fake** — 실제와 유사한 구현체 |
| 비즈니스 외적 부수효과 (알림 등) | **Dummy** — 아무것도 안 하는 구현체 |
| 데이터 확보 어려운 내부 서비스 | **Stub** — `given(...).willReturn(...)` |
| 그 외 | **실제 객체** |

### Fixture 클래스

도메인 객체 생성 코드는 `src/test/java/.../fixture` 패키지에 분리.

```java
public class SubscriptionFixture {

    public static Subscription activeSubscription() {
        return new Subscription(1L, 10L, LegislationType.EDUCATION, true);
    }

    public static Subscription inactiveSubscription() {
        return new Subscription(1L, 10L, LegislationType.EDUCATION, false);
    }
}
```

### 테스트 금지 패턴

```java
// BAD — 테스트 간 상태 공유 (Independent 위반)
static Subscription subscription = new Subscription(...);

// BAD — Thread.sleep 사용
Thread.sleep(100);

// BAD — System.out.println
System.out.println(result);

// BAD — 단일 테스트에 여러 시나리오 혼재
void testAll() { /* 여러 시나리오 모두 */ }
```

---

## 11. 커밋 & 브랜치 & PR 규칙

### 커밋 형식

```
{type} : {한국어 설명}
```

**space-colon-space (` : `) 필수.** 설명은 한국어.

예시:
- `feat : BillPost 조회수 중복 카운팅 방지 캐시 도입`
- `fix : CaffeineCacheManager ClassNotFoundException 수정`
- `refactor : core:domain BC별 패키지 재구성`
- `test : Subscription deactivate 비즈니스 규칙 테스트 추가`
- `lint : lint 적용`

### 커밋 단위 원칙

- **기능/목적 단위**로 커밋 분리
- lint 적용은 별도 커밋 (`lint : lint 적용`) — 항상 마지막
- 불필요한 포매팅 변경과 로직 변경을 같은 커밋에 섞지 않는다

### 브랜치 규칙 요약

**기준 브랜치**: 모든 작업은 `develop`에서 분기. **main 브랜치에 직접 커밋/force push 절대 금지.**

### PR 생성 명령

```bash
# 단일 브랜치 PR
gh pr create --base develop --title "{제목}" --body "$(cat .github/PULL_REQUEST_TEMPLATE.md)"

# 서브 브랜치 PR (베이스 브랜치 지정)
gh pr create --base feat/issue/{번호} --title "{제목}" --body "$(cat .github/PULL_REQUEST_TEMPLATE.md)"
```

---

## 12. /harness-check — 최종 관문

### 실행 범위

| 실행 맥락 | 검증 범위 |
|---|---|
| WORKFLOW.md Phase 6 게이트 | 이번 세션에서 작성·수정한 **전체 파일** |
| arch-plan Task 완료 검증 | 해당 **Task에서 변경한 파일만** |
| 독립 실행 (`/harness-check {경로}`) | 명시한 파일/모듈만 (`git diff HEAD` 기준) |

### 체크리스트 요약

**Architecture:**
- `core:domain`에 `@Service`, `@Component`, `@Autowired`, `@Transactional` 없음
- `core:domain`에 `@Entity`, JPA 어노테이션 없음
- `core:service` Business Layer에서 `infra:*` 클래스 직접 import 없음
- `core:service` Business Layer에서 다른 `@Service` 직접 주입 없음
- `app:api` Controller에서 `infra:*` 클래스 직접 import 없음

**Domain:**
- 도메인 객체에 setter 없음. 상태 변경 = 새 인스턴스 반환
- AR: `class` 타입. `record` 금지
- VO (행위 있는 불변): `final class` 타입
- VO (순수 데이터 홀더) / Read Model / Command / Query: `record` 타입
- `record`에 도메인 행위 없음
- AR 간 참조: `long` ID-only

**Service:**
- Business Layer (`@Service`): 모든 public 메서드에 `@Transactional` 존재
- Implement Layer (`@Component`): `@Transactional` 없음

**Error Handling:**
- 도메인 비즈니스 규칙 위반: `CoreDomainException` 하위 BC별 클래스 + static factory 사용
- `CoreDomainException` 직접 throw 없음
- `Exception` / `RuntimeException` 직접 상속 없음

**Testing:**
- 메서드명: `테스트대상_상태_기대결과` 형식
- `@DisplayName`: 완전한 한글 문장
- given / when / then 주석 구분

### 출력 형식

```
✅ PASS: [항목]
❌ FAIL: [항목] — [설명] → [수정 방향]
⚠️  WARN: [항목] — [권장사항]

총 PASS: N / FAIL: N / WARN: N
```

**FAIL이 1개라도 있으면 코드 제출 전 반드시 수정한다.**

---

## 13. 아키텍처 재설계 파이프라인

### 언제 사용하는가

Tier 3로 판정된 이슈. 재설계가 필요한 큰 기술 결정.

### 입력 변수

| 변수 | 설명 | 예시 |
|---|---|---|
| `[BUSINESS_GOAL]` | 이 시스템이 달성해야 할 핵심 비즈니스 목표 | "선착순 1만 명 동시 처리" |
| `[AS_IS_SCOPE]` | 재설계 대상 파일/패키지 경로 | `business/order`, `domain/order` |
| `[FROZEN_ZONE]` | 절대 변경 불가 항목 | "DB 스키마, POST /orders API 계약" |

입력이 불완전하면 먼저 `/dev-analyze`로 초안을 도출하고 사용자 확인을 받는다.

### Stage 0 — 전략 진단

5가지 현대화 전략 기준으로 진단:

```
① Rehosting     — 코드 변경 최소화, 환경만 이전
② Refactoring   — 외부 동작 유지, 내부 코드 구조만 개선
③ Replatforming — 플랫폼 이전 + 일부 코드 조정
④ Redesign      — 아키텍처 전면 재설계
⑤ Full Replace  — 전체 재작성
```

사용자와 합의 후 `[STRATEGY]`를 확정한 뒤 Stage 1로 진행.

### Stage 1 — AS-IS 코드베이스 이해 (Mapping)

`[AS_IS_SCOPE]` 내 모든 파일을 읽어 다음을 도출:
- 책임 목록, 의존성 방향, 데이터 흐름
- 트랜잭션 경계, 상태 변이점
- **스키마 스냅샷** (Stage 6 교차 검증용 — 반드시 포함)

### Stage 2 — 비판적 해부 (7-Lens Critique)

| 렌즈 | 핵심 질문 |
|---|---|
| **L1 Performance & Scalability** | 트래픽이 10~100배 증가할 때 가장 먼저 터지는 병목은? |
| **L2 Reliability & Consistency** | 부분 실패 시 데이터 정합성이 깨지는 지점은? |
| **L3 Anti-patterns** | 불필요하게 강결합되거나 동기 처리되어 레이턴시를 깎아먹는 구간은? |
| **L4 도메인 모델** | 비즈니스 규칙이 도메인 외부로 유출되어 있는가? (Anemic Domain?) |
| **L5 테스트 가능성** | 단위 테스트 작성이 구조적으로 불가능한 곳은? |
| **L6 변경 용이성** | 비즈니스 규칙 하나를 바꿀 때 몇 개 파일이 연쇄 수정되는가? |
| **L7 숨겨진 결합** | 암묵적 호출 순서 의존성, 시간적 결합이 있는가? |

각 렌즈마다 코드 위치(파일명:라인)를 증거로 첨부.

### Stage 3 — 역질문 (Reverse Questioning)

Stage 2의 발견을 바탕으로 부족한 정보를 질문. **질문 후 즉시 대기** — 사용자 답변 후 Stage 4 진행.

### Stage 4 — 정량적 트레이드오프 분석

수치로 AS-IS 한계를 증명하고 TO-BE 예상 개선치를 계산.

```
[AS-IS] DB Connection Pool 20개, 요청당 점유 500ms
        최대 처리량 = 20 / 0.5s = 40 TPS

[TO-BE] 비동기 처리로 커넥션 점유 10ms로 단축
        최대 처리량 = 20 / 0.01s = 2,000 TPS (50배 향상)
```

### Stage 5 — TO-BE 패러다임 제안

패러다임 인벤토리를 전부 검토한 후 대안 A / 대안 B를 제시.

**[구조]**: Modular Monolith, Hexagonal/Clean Architecture, Microservices, Strangler Fig
**[데이터]**: CQRS, Event Sourcing
**[분산 일관성]**: Saga Choreography, Saga Orchestration, TCC, Outbox Pattern

선택하지 않는 패턴은 "부적합 이유"를 반드시 명시.

### Stage 6 — ADR 생성 및 파일 저장

**ADR 작성 전 반드시 실행 (설계-코드 교차 검증):**
- 추가/변경 제안하는 필드·컬럼이 이미 존재하는지 Stage 1 목록에서 확인
- 프레임워크 API 정확성 검증
- FROZEN_ZONE 최종 확인

**ADR 파일 경로**: `docs/adr/ARCH-{NNN}-{YYYY-MM-DD}-{topic}.md`

ADR 승인 후 구현 시: `/project:pipeline:arch-plan {topic} adr={ADR경로}`

---

## 14. 구현 계획 수립 파이프라인

### 두 가지 모드

**모드 A — 독립 계획 수립 (ADR 없음)**
- Tier 2 복잡 케이스 또는 개발자 직접 호출
- 탐색적 성격: 여러 구현 방향을 검토하고 확정

```bash
/arch-plan order-expiry-batch-limit
```

**모드 B — ADR 기반 구현 계획 (ADR 있음)**
- Tier 3 → /arch-redesign 완료 후 진입
- ADR 방향에서 벗어나는 Task 제안 금지

```bash
/arch-plan order-outbox-migration adr=docs/adr/ARCH-001-2026-03-03-order-redesign.md
```

### Step 0 — 범위 추론 및 확인

사용자 요청과 ADR을 바탕으로 영향 scope를 추론하고 사용자 확인 후 Step 1 진행.

### Step 1 — 코드베이스 분해

확정된 scope의 모든 파일을 읽어 책임 목록, 의존성 방향, 상태변이 지점, 트랜잭션 경계를 도출.

### Step 2 — 구현 계획 수립 (피드백 루프)

- Task 단위: 단일 기술 레이어가 아닌 **비즈니스 흐름 단위**
- 사용자 피드백 → 계획 수정 → 재제시를 "확정" 신호가 올 때까지 반복

### Step 3 — 계획 파일 저장

파일 경로: `docs/plans/{YYYY-MM-DD}-{TOPIC}.md`

파일에는 Task별 하네스 체크포인트와 완료 조건(`/harness-check` FAIL 0개) 포함.

---

## 15. 경우의 수 총정리

### Q1. 이슈를 받았을 때 가장 먼저 무엇을 하는가?

**A**: Tier 판정. 5가지 축으로 Tier 1/2/3을 결정하고, 결과를 채팅에 출력한다.

### Q2. Tier 1인데 Tier 2로 의심될 때?

**A**: 오분류 원칙 적용 — 항상 더 보수적인 Tier로. Tier 2로 진행한다.

### Q3. Tier 2인데 직접 진행하고 싶다면?

**A**: 개발자가 `/project:tier:dev-auto`를 명시적으로 호출할 수 있다. 단, 안전 거부 조건(DB 스키마 변경, API 계약 변경, 보안 변경)에 해당하면 거부된다.

### Q4. Tier 2에서 구현계획.md를 작성했는데 사용자가 아직 승인 안 했다면?

**A**: 코드 작성을 절대 시작하지 않는다. "진행해" 입력을 기다린다.

### Q5. Tier 3 이슈인데 개발자가 "코드 바로 짜줘"라고 하면?

**A**: 거부한다. Tier 3은 ADR 승인 전까지 코드 작성 일체 금지. ADR을 먼저 작성하고 승인받아야 한다.

### Q6. 새 BC가 필요한지 불명확할 때?

**A**: 코드 작성을 멈추고 사용자에게 확인을 구한다. 아키텍처 결정은 개발자가 내려야 한다.

### Q7. 두 BC 중 어느 쪽에 로직을 배치할지 모호할 때?

**A**: 코드 작성을 멈추고 도메인 경계 결정을 사용자에게 요청한다.

### Q8. 테스트가 실패하면?

**A**: 원인을 분석하고 코드를 수정한다. 같은 명령을 반복 재시도하지 않는다. 외부 요인(환경, 데이터)이 원인이면 사용자에게 확인을 구한다.

### Q9. harness-check에서 FAIL이 나오면?

**A**: FAIL이 1개라도 있으면 커밋하지 않고 해당 항목을 수정한 후 재검증한다.

### Q10. 변경 파일이 12개를 초과할 것 같을 때?

**A**: 서브 브랜치 전략으로 전환한다. 레이어별로 브랜치를 분리하고 순차적으로 PR을 올린다.

### Q11. PR에 domain과 api 레이어가 함께 있어도 되는가?

**A**: 금지. 2단계 건너뛰기는 불가. domain + service, service + api는 허용이지만 domain + api는 금지.

### Q12. core:service에서 infra:storage의 JPA Repository를 직접 주입해도 되는가?

**A**: 절대 불가. core:service Business Layer는 Implement Layer(Reader/Manager)를 통해서만 데이터에 접근한다. infra:* 직접 참조 금지.

### Q13. Aggregate Root에 @Entity를 붙여도 되는가?

**A**: 절대 불가. core:domain은 순수 POJO. @Entity는 infra:storage의 JpaEntity에만 붙는다.

### Q14. 비즈니스 규칙 위반 시 IllegalStateException을 던져도 되는가?

**A**: 불가. 반드시 BC별 `{BC명}DomainException extends CoreDomainException` + static factory를 사용한다.

### Q15. Service에서 다른 Service를 주입해도 되는가?

**A**: 불가. Business Service 간 직접 주입 금지. 여러 Service를 조합해야 할 때는 Facade를 만든다.

### Q16. core:domain에 UseCase 단위 패키지 (`account/create/`, `home/`)를 만들어도 되는가?

**A**: 불가. core:domain은 Aggregate 단위 패키지만 허용. UseCase/feature 단위 패키지는 core:service에만 허용.

### Q17. Read Model을 core:domain에 두어야 하는가, core:service에 두어야 하는가?

**A**:
- 단일 Aggregate 투영 → `core:domain` 해당 Aggregate 패키지
- 크로스 Aggregate 조합 (여러 Repository 결과 합치기) → `core:service` feature 패키지

### Q18. Command/Query를 core:service에 두어도 되는가?

**A**: 불가. 모든 Command/Query는 `core:domain`에 정의한다. core:service에 두면 core:domain이 역방향 참조하게 된다.

### Q19. /arch-redesign과 /arch-plan의 차이는?

**A**:
- `/arch-redesign`: "현재 설계의 무엇이 문제인가, 어떻게 바꿔야 하는가"를 분석하고 ADR을 생성한다. 코드는 작성하지 않는다.
- `/arch-plan`: 구체적으로 어떤 파일을 어떻게 바꿀지 구현 계획을 세운다. 코드 작성의 전 단계.

### Q20. docs/DOMAIN_ENCYCLOPEDIA.md는 언제 업데이트하는가?

**A**: 새 BC/클래스 추가 또는 리네임 시 반드시 코드 작업과 함께 업데이트한다.

---

## 16. 자주 하는 실수 & 위반 패턴

### 도메인 계층 (core:domain)

| 실수 | 올바른 방향 |
|---|---|
| `@Entity`, `@Service`, `@Component` 추가 | 순수 POJO 유지 |
| `setter`로 도메인 객체 상태 변경 | `with*/activate/deactivate/modify*` 패턴 |
| `record`에 도메인 행위 추가 | `final class` 또는 `class`로 전환 |
| AR 간 객체 직접 참조 | `long` ID-only 참조 |
| Command/Query를 `core:service`에 정의 | `core:domain`에 정의 |
| 비즈니스 규칙을 Service에 위치 | Domain 객체 내부에 캡슐화 |
| 도메인 패키지를 UseCase 기준으로 분류 | Aggregate 단위로 분류 |
| `UserExternalAuth`를 record로 선언 (has() 행위 있음) | class로 선언 |

### 서비스 계층 (core:service)

| 실수 | 올바른 방향 |
|---|---|
| `@Service`에 `@Transactional` 없음 | 모든 public 메서드에 필수 |
| Business Service에서 `infra:*` 직접 주입 | Implement Layer 경유 |
| Business Service 간 직접 주입 | Facade로 상위 레이어에서 조합 |
| Implement Layer가 Business Layer를 알고 있음 | 역방향 참조 금지 |
| Controller에서 Reader 직접 호출 | Business Layer(Service) 경유 |

### Presentation 계층 (app:api)

| 실수 | 올바른 방향 |
|---|---|
| Request 객체를 Service에 직접 전달 | Request → Command 변환 후 전달 |
| Request 검증 로직이 Service 내부에 있음 | Controller 진입 시점에 `validate()` 호출 |
| `infra:*` 클래스 직접 import | runtimeOnly — 컴파일 타임 접근 불가 |

### 예외 처리

| 실수 | 올바른 방향 |
|---|---|
| 비즈니스 규칙 위반에 `IllegalStateException` | `CoreDomainException` 하위 + static factory |
| 도메인에서 `CoreApiException` throw | `CoreDomainException` 사용 |
| `CoreDomainException` 직접 throw | BC별 하위 예외 클래스 사용 |
| 새 예외 클래스가 `Exception` 직접 상속 | `CoreDomainException` 또는 `CoreApiException` 상속 |
| `IMPLEMENTATION` 레벨인데 WARN 로그 | ERROR 로그 + Alert 필수 |

### 테스트

| 실수 | 올바른 방향 |
|---|---|
| `static` 필드로 테스트 간 상태 공유 | 각 테스트에서 독립적으로 생성 |
| 도메인 테스트에 `@Tag("develop")` | 태그 없음, 순수 Java |
| 통합 테스트에 `DevelopTest` 미상속 | `extends DevelopTest` 필수 |
| 메서드명이 동사로 시작 (`testValidate`) | `테스트대상_상태_기대결과` 형식 |

### 커밋 & 브랜치

| 실수 | 올바른 방향 |
|---|---|
| 린트와 로직 변경을 같은 커밋에 혼합 | 별도 커밋 (`lint : lint 적용`) |
| `git push --force` (main 브랜치) | 절대 금지 |
| domain + api를 동일 PR에 포함 | 2단계 건너뛰기 금지 |
| PR에 600줄 이상 변경 | 기능 단위 분할 |
| spotlessApply 없이 커밋 | 코드 변경 후 반드시 실행 |

---

## 부록 — 빌드 & 린트 명령 레퍼런스

```bash
# 전체 빌드
./gradlew build

# 린트
./gradlew spotlessApply     # 린트 적용 (코드 변경 후 반드시 실행)
./gradlew spotlessCheck     # 린트 검사 (CI)

# 테스트
./gradlew test              # 기본 테스트
./gradlew unitTest          # 순수 유닛 테스트 (태그 없음)
./gradlew developTest       # Spring 컨텍스트 통합 테스트 (@Tag("develop"))
./gradlew contextTest       # DB 컨텍스트 테스트 (@Tag("context"))
./gradlew restDocsTest      # REST Docs 문서 생성 테스트 (@Tag("restdocs"))

# 특정 모듈 테스트
./gradlew :core:domain:test
./gradlew :core:service:test
./gradlew :app:api:test

# 컴파일 확인
./gradlew :core:domain:compileJava
./gradlew :core:service:compileJava
./gradlew :app:api:compileJava
```

## 부록 — 코드 제출 전 체크리스트

```
[ ] docs/DOMAIN_ENCYCLOPEDIA.md 확인 (새 BC/클래스 추가 시 업데이트)
[ ] ./gradlew spotlessApply 실행
[ ] ./gradlew unitTest 통과
[ ] ./gradlew developTest 통과 (통합 테스트 포함 시)
[ ] /project:harness-check FAIL 0개
[ ] 커밋 형식 확인 ({type} : {한국어 설명})
[ ] lint 커밋이 마지막인지 확인
[ ] PR 크기 확인 (파일 ≤ 12개, 라인 ≤ 600줄)
[ ] 브랜치 기준 확인 (develop에서 분기)
```
