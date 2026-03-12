# coding-rules

각 파일은 해당 레이어 구현 진입 시점에 로드한다.

---

## 로드 조건

| 파일 | 로드 시점 |
|---|---|
| `ARCHITECTURE.md` | start-task 설계·계획 단계 — 즉시 로드 |
| `DOMAIN_RULES.md` | `core:domain` 구현 시작 직전 |
| `SERVICE_RULES.md` | `core:service` 구현 시작 직전 |
| `ERROR_HANDLING.md` | 예외 클래스 신규 추가 시 |
| `TESTING.md` | `/dev:continue-task` Step 3 진입 시 / 테스트 에이전트 내부 |

---

## 핵심 5규칙 (CLAUDE.md §1 참조)

1. 의존성 방향: `app → core:service → core:domain ← infra`
2. Domain POJO: `@Entity`, `@Service`, `@Transactional` 금지. 불변 with 패턴.
3. Business Layer: `infra:*` 직접 import 금지. 다른 `@Service` 직접 주입 금지.
4. 예외: `CoreDomainException` 하위 + static factory. `BUSINESS`=WARN, `IMPLEMENTATION`=ERROR+Alert.
5. AR 간 참조: ID-only (`long` 타입).