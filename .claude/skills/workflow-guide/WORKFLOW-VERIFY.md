# Phase 4 — 테스트 & 검증

---

## 테스트 Agent 실행 (병렬)

| 조건 | Agent |
|---|---|
| `app:api` 변경 시 | `test-app-api` |
| `app:batch` 변경 시 | `test-app-batch` |
| `core:domain` 변경 시 | `test-core-domain` |
| `core:service` 순수 로직 변경 시 | `test-core-service` |
| `app:api` 신규 엔드포인트 시 | `test-app-api-docs` |

---

## 검증 명령어

```bash
./gradlew spotlessApply        # 린트 적용
./gradlew unitTest             # 단위 테스트
./gradlew contextTest          # 인수 테스트 (DB)
/harness-check                 # 아키텍처 규칙 검증
```

**harness-check FAIL 시 커밋 금지.** 원인 분석 후 코드 수정. 동일 명령 반복 재시도 금지.

---

## 완료 기준

위 명령어 전체 PASS + harness-check PASS → Phase 5(커밋 & PR) 진행.