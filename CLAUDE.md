# Barlow — Claude 작업 지침

---

## 0. 작업 시작 — 이슈/요구사항 접수 시 가장 먼저 실행

**경로에 따라 아래 중 하나를 선택한다.**

- **파이프라인 사용 시**: `/dev:start-task {issue_number}` — 라우팅·코드 분석·규칙 적용 자동 처리
- **직접 코드 작업 시**: `coding-rules` 스킬 수동 로드 + `docs/DOMAIN_ENCYCLOPEDIA.md` 해당 BC 섹션 읽기

---

## 1. 핵심 아키텍처 규칙

1. **의존성 방향**: `app → core:service → core:domain ← infra` (단방향, 역방향 금지)
2. **Business Layer**: `infra:*` 직접 주입 금지. Implement Layer(Reader/Manager) 경유
3. **Domain**: 순수 POJO. Spring 어노테이션 금지. setter 금지. 상태 변경 = 새 인스턴스 반환
4. **예외**: `CoreDomainException` 하위 + static factory. HTTP 의존 금지
5. **AR 간 참조**: ID-only (`long` 타입). 다른 AR 객체 직접 참조 금지
6. **구현 순서**: `core:domain` → `core:service` → `infra:storage` → `app:api`

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
| 워크플로우 | `.claude/commands/dev/` |
| 아키텍처 & 코딩 규칙 | `.claude/skills/coding-rules/` |
| ADR | `docs/adr/` |