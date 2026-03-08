# Phase 5~6 — 테스트 & 검증

---

## Phase 5 — 테스트 작성

> **TESTING.md 로드**: Phase 5 진입 시점에 `.claude/skills/coding-rules/TESTING.md`를 읽는다.
> Phase 0에서 선취하지 않았으므로 여기서 반드시 로드한다.

### 5-1. 테스트 전략

**비즈니스 레이어(`@Service`, Reader, Handler 등) Mock 단위 테스트는 작성하지 않는다.**
**Repository 테스트는 별도로 작성하지 않는다.**
인수 테스트가 HTTP → Service → DB 전체를 end-to-end로 검증한다.

| 테스트 종류 | 대상 | Agent | CI |
|---|---|---|---|
| 도메인 정책 테스트 (unitTest) | 상태 전이 불변 규칙, Policy, 계산 로직 | `test-core-domain` | O |
| 인수 테스트 (@AcceptanceTest) | 사용자 여정 end-to-end | `test-app-api` | O |
| 순수 로직 테스트 (unitTest) | core:service 전략 클래스 등 | `test-core-service` | O |
| API 문서화 테스트 (RestDocsContextTest) | app:api 엔드포인트 스펙 문서화 | `test-app-api-docs` | X (api-docs.yml) |
| 인프라 라이브러리 테스트 | JWT, FCM 등 라이브러리 동작 | `test-infra-auth`, `test-infra-notification` | X |

### 5-2. Agent 병렬 실행

변경 범위에 따라 해당하는 agent를 **병렬로** 실행한다.

```
[항상] test-core-domain  ─┐
[항상] test-app-api       ├─ 병렬 실행 (Agent 툴 사용)
[해당 시] test-core-service ┘

[선택] test-infra-auth        ← infra:auth 변경이 있을 때만
[선택] test-infra-notification ← infra:notification 변경이 있을 때만
[선택] test-app-api-docs      ← app:api 엔드포인트 추가/변경 시
```

**병렬 실행 규칙**: 모듈이 다르면 의존성이 없으므로 동시에 실행 가능하다.
각 agent는 해당 모듈의 테스트를 작성하고 `./gradlew :{module}:unitTest` 또는 `contextTest`로 검증 후 보고한다.

### 5-3. 테스트 품질 체크리스트
- [ ] 메서드명: `테스트대상_상태_기대결과` 형식
- [ ] @DisplayName: 완전한 한글 문장 ("~할 때, ~하면, ~한다")
- [ ] given/when/then 주석 구분
- [ ] 테스트 간 상태 공유 없음 (static 상태 금지)
- [ ] VO는 무조건 실제 객체 (SemanticVersion 등 Mock 금지)
- [ ] 비즈니스 레이어 Mock 테스트 없음
- [ ] Repository 테스트 별도 없음
- [ ] 단순 정책 → flat / 복잡한 정책 → @Nested BDD

---

## Phase 6 — 린트 & 검증

```bash
# 1. 린트 적용
./gradlew spotlessApply

# 2. 단위 테스트 실행 (CI 포함 — 항상 실행)
./gradlew unitTest

# 3. 인수 테스트 실행 (CI 포함 — 항상 실행)
./gradlew contextTest

# 4. 컴파일 확인
./gradlew :core:domain:compileJava
./gradlew :core:service:compileJava
./gradlew :app:api:compileJava

# [선택] 로컬 실험 테스트 실행 (CI 미포함)
./gradlew developTest
```

> **태그 기준**: `unitTest` + `contextTest` = CI 실행. `developTest` = 로컬 실험 전용.

테스트 실패 시 → 원인 분석 후 코드 수정. 같은 실패를 반복 재시도하지 않는다.

### 6-5. Harness 규칙 검증 (최종 관문)

```bash
/project:harness-check  # 이번 세션 전체 변경 파일 대상
```

- FAIL 항목이 1개라도 있으면 커밋하지 않고 수정 후 재검증한다.
- PASS 확인 후 Phase 7 커밋으로 진행한다.

---

## [세션 격리 체크포인트 B]

> **Phase 6 완료 (전체 테스트 PASS, Harness PASS) 직후 권장**
>
> 구현 세션의 탐색·디버깅 이력을 정리하고 커밋/PR에 집중한다.
>
> ```
> 1. MEMORY.md의 currentWork 단계를 "Phase 6 완료" 로 업데이트한다
> 2. /clear 로 대화 이력을 초기화한다
> 3. 새 세션에서 "Phase 7 진행해 — 이슈 #{번호}" 로 재개한다
> ```