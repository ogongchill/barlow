# Barlow — Claude 작업 지침

---

## 0. 작업 시작 — 이슈/요구사항 접수 시 가장 먼저 실행

**모든 개발 작업 전에 아래 순서대로 실행한다.**

1. `workflow-guide` 스킬 로드
2. `coding-rules` 스킬 로드 — 코드 작업 시
3. `docs/DOMAIN_ENCYCLOPEDIA.md` 해당 BC 섹션 읽기

---

## 1. 핵심 아키텍처 규칙

1. **의존성 방향**: `app → core:service → core:domain ← infra` (단방향, 역방향 금지)
2. **Business Layer**: `infra:*` 직접 주입 금지. Implement Layer(Reader/Manager) 경유
3. **Domain**: 순수 POJO. Spring 어노테이션 금지. setter 금지. 상태 변경 = 새 인스턴스 반환
4. **예외**: `CoreDomainException` 하위 + static factory. HTTP 의존 금지
5. **구현 순서**: `core:domain` → `core:service` → `infra:storage` → `app:api`

> 상세 규칙: `.claude/skills/coding-rules/` 참조

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

**커밋 단위**: 레이어 단위 분리. lint는 항상 마지막 커밋.

---

## 3. 테스트 태그 빠른 참조

| 태그 | 실행 명령 | 의미 |
|---|---|---|
| 없음 | `./gradlew unitTest` | 순수 Java 단위 테스트 |
| `@Tag("context")` | `./gradlew contextTest` | DB 인수 테스트 |
| `@Tag("restdocs")` | `./gradlew restDocsTest` | REST Docs 문서 생성 테스트 |
| `@Tag("develop")` | `./gradlew developTest` | 로컬 실험 전용 (CI 미포함) |

---

## 4. 참조 문서

| 문서 | 위치 |
|---|---|
| 도메인 백과사전 | `docs/DOMAIN_ENCYCLOPEDIA.md` |
| 워크플로우 | `.claude/skills/workflow-guide/` |
| 아키텍처 & 코딩 규칙 | `.claude/skills/coding-rules/` |
| ADR | `docs/adr/` |