# Barlow — Claude 작업 지침

> 설정 파일. 레퍼런스 문서는 §3 참조.

---

## 0. 작업 시작 — 이슈/요구사항 접수 시 가장 먼저 실행

### 이슈 Tier 판정 (자동)
모든 이슈는 구현 전에 Tier를 판정한다. 판정 기준은 `WORKFLOW-TRIAGE.md` 참조.

| Tier | 조건 | 진행 방식 |
|---|---|---|
| **Tier 1** | 단일 BC, 명확한 스펙, 파일 ≤12개, API/DB 계약 변경 없음 | 자율 진행 (승인 없이 Phase 8까지) |
| **Tier 2** | 멀티 BC, 기존 동작 변경, 파일 >12개, 모호한 요구사항 | 계획.md 작성 후 승인 대기 |
| **Tier 3** | 재설계, trade-off 결정, 대규모 리팩토링, ADR 필요 | 분석 보고서만. 코드 작성 금지 |

**오분류 원칙**: 항상 더 보수적인 Tier로. 애매하면 상위 Tier 선택.

### 커스텀 커맨드 (Tier 강제 지정)

| 커맨드 | 동작 |
|---|---|
| `/project:tier:dev-auto` | Tier 1 강제 자율 진행 |
| `/project:tier:dev-plan` | Tier 2 계획 후 승인 모드 강제 |
| `/project:tier:dev-analyze` | Tier 3 사전 스크리닝 |

### 스킬 로드
**모든 코드 작성·수정·리뷰 작업 전에 아래 두 스킬을 순서대로 로드한다.**

1. `workflow-guide` — 개발 파이프라인 (항상 로드) → `WORKFLOW-TRIAGE.md` 부터 시작
2. `coding-rules` — 코딩 규칙 (코드 작업 시 추가 로드) → 로드 후 `docs/DOMAIN_ENCYCLOPEDIA.md` 해당 BC 섹션 읽기

---

## 1. 아키텍처 & 코딩 규칙

> **상세는 `coding-rules` 스킬 로드 후 해당 파일 참조.**

| 규칙 영역 | 파일 |
|---|---|
| 모듈 구조, 레이어 의존성, 4대 규칙 | `ARCHITECTURE.md` |
| core:domain 코딩 규칙 (타입 선택, 불변, 패키지) | `DOMAIN_RULES.md` |
| core:service + app:api 구현 규칙 | `SERVICE_RULES.md` |
| 예외 계층, HTTP 매핑, 로깅/알럿 | `ERROR_HANDLING.md` |
| 테스트 분류, 작성 기준, 구조 규칙 | `TESTING.md` |

---

## 2. 커밋 컨벤션

형식: `{type} : {한국어 설명}` — **space-colon-space** (` : `) 필수.

| 타입 | 사용 시점 |
|---|---|
| `feat` | 새 기능 추가 |
| `fix` | 버그 수정 |
| `refactor` | 리팩토링 (기능 변화 없음) |
| `rename` | 클래스/파일명 변경 |
| `lint` | 포매터 적용 |
| `gradle` | 빌드 설정 변경 |
| `batch` | 배치 관련 변경 |
| `chore` | 기타 설정 |
| `test` | 테스트 추가/수정 |
| `docs` | 문서 변경 |

**커밋 단위**: 기능/목적 단위 분리. lint는 별도 커밋.

---

## 3. 테스트 태그 빠른 참조

| 태그 | 실행 명령 | 의미 |
|---|---|---|
| 없음 | `./gradlew unitTest` | 순수 Java 단위 테스트 |
| `@Tag("develop")` | `./gradlew developTest` | Spring 컨텍스트 통합 테스트 |
| `@Tag("context")` | `./gradlew contextTest` | DB 컨텍스트 인수 테스트 |
| `@Tag("restdocs")` | `./gradlew restDocsTest` | REST Docs 문서 생성 테스트 |

---

## 4. 참조 문서

| 문서 | 위치 |
|---|---|
| 도메인 백과사전 | `docs/DOMAIN_ENCYCLOPEDIA.md` |
| 개발 파이프라인 (인덱스) | `.claude/skills/workflow-guide/WORKFLOW.md` |
| Tier 판정 | `.claude/skills/workflow-guide/WORKFLOW-TRIAGE.md` |
| 분석 & 계획 (Phase 0~3) | `.claude/skills/workflow-guide/WORKFLOW-PLAN.md` |
| 코드 구현 (Phase 4) | `.claude/skills/workflow-guide/WORKFLOW-IMPL.md` |
| 테스트 & 검증 (Phase 5~6) | `.claude/skills/workflow-guide/WORKFLOW-VERIFY.md` |
| 커밋 & PR (Phase 7~8) | `.claude/skills/workflow-guide/WORKFLOW-SHIP.md` |
| 아키텍처 규칙 | `.claude/skills/coding-rules/ARCHITECTURE.md` |
| 코딩 규칙 | `.claude/skills/coding-rules/` (DOMAIN_RULES, SERVICE_RULES, ERROR_HANDLING, TESTING) |
| 커맨드 | `.claude/commands/` (tier/, pipeline/, harness-check.md) |
| ADR | `docs/adr/` |