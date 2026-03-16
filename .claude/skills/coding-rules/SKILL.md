# coding-rules

각 파일은 **start-task Pipeline Step 3** 진입 시 레이어 기반 매트릭스에 따라 선택적으로 로드한다.
`continue-task`는 이 파일들을 직접 로드하지 않는다 — plan.md에 이미 적용 결과가 반영되어 있다.

---

## 로드 조건 (start-task Pipeline Step 3 기준)

| 파일 | 로드 조건 |
|---|---|
| `ARCHITECTURE.md` | 어떤 유형이든 — **항상** 로드 (Step 1에서도 로드) |
| `DOMAIN_RULES.md` | scope.md 타겟 파일에 `core:domain` 레이어 포함 시 |
| `SERVICE_RULES.md` | scope.md 타겟 파일에 `core:service` 또는 `app:api` 레이어 포함 시 |
| `REST_API_RULES.md` | scope.md 타겟 파일에 `app:api` 레이어 포함 시 |
| `ERROR_HANDLING.md` | scope.md 타겟 파일에 신규 예외 클래스 추가가 포함 시 |
| `TESTING.md` | 테스트 에이전트 내부 — `test-app-batch`, `test-app-api` 등이 참조 |

**레이어 기반 매트릭스 예시:**

| 이슈 유형 | 로드 파일 |
|---|---|
| BATCH (app:batch + infra:storage/batch만 변경) | `ARCHITECTURE.md` |
| API (core:domain + core:service + infra + app:api) | `ARCHITECTURE.md` + `DOMAIN_RULES.md` + `SERVICE_RULES.md` + `REST_API_RULES.md` |
| API + 신규 예외 | 위 + `ERROR_HANDLING.md` |
| DOMAIN만 | `ARCHITECTURE.md` + `DOMAIN_RULES.md` |

---

## 핵심 5규칙 (CLAUDE.md §1 참조)

1. 의존성 방향: `app → core:service → core:domain ← infra`
2. Domain POJO: `@Entity`, `@Service`, `@Transactional` 금지. 불변 with 패턴.
3. Business Layer: `infra:*` 직접 import 금지. 다른 `@Service` 직접 주입 금지.
4. 예외: `CoreDomainException` 하위 + static factory. `BUSINESS`=WARN, `IMPLEMENTATION`=ERROR+Alert.
5. AR 간 참조: ID-only (`long` 타입).