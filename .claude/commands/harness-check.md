# /harness-check

작성된 코드가 Barlow Harness 가이드라인을 준수하는지 검증한다.

## 실행 범위

이 커맨드는 세 가지 맥락에서 사용된다. **실행 범위가 다르므로** 맥락을 먼저 확인한다.

| 실행 맥락 | 검증 범위 |
|----------|---------|
| WORKFLOW.md Phase 6 게이트 | 이번 세션에서 작성·수정한 **전체 파일** |
| arch-plan Task 완료 검증 | 해당 **Task에서 변경한 파일만** |
| 독립 실행 (`/harness-check {경로}`) | 명시한 파일/모듈만. 범위 미명시 시 `git diff HEAD` 기준 |

맥락이 불명확하면 사용자에게 확인한다.

## 실행 순서

1. `.claude/skills/coding-rules/ARCHITECTURE.md` 읽기
2. `.claude/skills/coding-rules/DOMAIN_RULES.md` 읽기
3. `.claude/skills/coding-rules/SERVICE_RULES.md` 읽기
4. `.claude/skills/coding-rules/ERROR_HANDLING.md` 읽기
5. `.claude/skills/coding-rules/TESTING.md` 읽기
6. 위 범위 기준으로 대상 파일을 확정하고 아래 체크리스트 실행

---

## 체크리스트

### Architecture — 레이어 의존성

- [ ] `core:domain` 클래스에 `@Service`, `@Component`, `@Autowired`, `@Transactional` 없음
- [ ] `core:domain` 클래스에 `@Entity`, JPA 어노테이션 없음
- [ ] `core:service` Business Layer (`@Service`) 에서 `infra:*` 클래스 직접 import 없음
- [ ] `core:service` Business Layer 에서 다른 `@Service` 직접 주입 없음 (Facade로만 조합)
- [ ] `core:service` Implement Layer (`@Component`) 에서 Port Interface만 통해 infra 통신
- [ ] `app:api` Controller에서 `infra:*` 클래스 직접 import 없음

### Domain — 불변 객체 & 타입 선택

- [ ] 도메인 객체에 setter 없음. 상태 변경 = 새 인스턴스 반환 (`with*/activate/deactivate/modify*`)
- [ ] AR (Aggregate Root): `class` 타입. `record` 금지
- [ ] VO (행위 있는 불변): `final class` 타입
- [ ] VO (순수 데이터 홀더) / Read Model / Command / Query: `record` 타입
- [ ] `record`에 도메인 행위(비즈니스 검증, 계산) 없음
- [ ] AR 간 참조: `long` ID-only. 객체 직접 참조 금지
- [ ] 외부 생성자 대신 static factory 메서드 사용

### Service — 트랜잭션 & 주입

- [ ] `@Component`(Implement Layer)에 `@Transactional` 없음 — Impl에 `@Transactional`은 일절 금지
- [ ] Business Layer (`@Service`): DB 쓰기를 포함한 메서드에 `@Transactional` 선언됨
- [ ] Business Layer (`@Service`): 단순 읽기 위임 / `@Cacheable` 메서드에 불필요한 `@Transactional` 없음
- [ ] `@Cacheable`과 `@Transactional`을 같은 메서드에 병용하지 않음
- [ ] `Reader` 클래스에 쓰기(save/update/delete) 메서드 없음 — 쓰기는 `Updater`/`Manager`/`Processor`로 분리

### Error Handling — 예외 계층

- [ ] 도메인 비즈니스 규칙 위반: `CoreDomainException` 하위 BC별 클래스 + static factory 사용
- [ ] `IllegalStateException` / `IllegalArgumentException`을 비즈니스 규칙 위반에 사용하지 않음
- [ ] `CoreDomainException` 직접 throw 없음 (반드시 BC별 하위 클래스 사용)
- [ ] `Exception` / `RuntimeException` 직접 상속 없음
- [ ] API 레이어 예외: `CoreApiException` 사용 (도메인에서 `CoreApiException` throw 금지)
- [ ] `BUSINESS` 레벨 예외 (정상 거절): WARN 로그
- [ ] `IMPLEMENTATION` 레벨 예외 (버그 가능성): ERROR 로그 + Alert 설정

### Testing — 작성 기준

- [ ] 도메인 정책 테스트: 태그 없음, Spring 컨텍스트 없이 순수 Java로 실행
- [ ] 통합 테스트: `@Tag("develop")` + `extends DevelopTest`
- [ ] 인수 테스트 (@AcceptanceTest): `@Tag("context")` + `extends ContextTest`
- [ ] 메서드명: `테스트대상_상태_기대결과` 형식 (`validate_RequiredTermNotAgreed_ThrowsException`)
- [ ] `@DisplayName`: 완전한 한글 문장 ("~할 때, ~하면, ~한다/된다")
- [ ] given / when / then 주석 구분
- [ ] `static` 필드로 테스트 간 상태 공유 없음
- [ ] `Thread.sleep()` 사용 없음
- [ ] 도메인 테스트: Mockito 최소화, 실제 객체 우선

---

## 출력 형식

```
✅ PASS: [항목]
❌ FAIL: [항목] — [설명] → [수정 방향]
⚠️  WARN: [항목] — [권장사항]

총 PASS: N / FAIL: N / WARN: N
```

FAIL이 1개라도 있으면 코드 제출 전 반드시 수정한다.