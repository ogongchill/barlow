# /harness-check

작성된 코드가 Barlow Harness 가이드라인을 준수하는지 검증한다.

## 실행 범위

이 커맨드는 세 가지 맥락에서 사용된다. **실행 범위가 다르므로** 맥락을 먼저 확인한다.

| 실행 맥락 | 검증 범위 |
|----------|---------|
| /continue-task 검증 단계 | 이번 세션에서 작성·수정한 **전체 파일** |
| arch-plan Task 완료 검증 | 해당 **Task에서 변경한 파일만** |
| 독립 실행 (`/harness-check {경로}`) | 명시한 파일/모듈만. 범위 미명시 시 `git diff HEAD` 기준 |

맥락이 불명확하면 사용자에게 확인한다.

## 실행 순서

1. 위 범위 기준으로 대상 파일을 확정하고 아래 체크리스트 실행
   - 특정 항목의 판단 기준이 불명확하면 해당 규칙 파일만 선택적으로 읽는다:
     `ARCHITECTURE.md` / `DOMAIN_RULES.md` / `SERVICE_RULES.md` / `ERROR_HANDLING.md` / `TESTING.md`

---

## 체크리스트

### Architecture — 레이어 의존성

- [ ] `core:domain` 클래스는 순수 POJO: `@Service`, `@Component`, `@Autowired`, `@Transactional` 제거
- [ ] `core:domain` 클래스는 JPA 무관: `@Entity` 및 JPA 어노테이션 제거
- [ ] `core:service` Business Layer (`@Service`): Implement Layer만 주입. `infra:*` 직접 import 제거
- [ ] `core:service` Business Layer: 다른 `@Service` 직접 주입 제거 (Facade로만 조합)
- [ ] `core:service` Implement Layer (`@Component`): Port Interface 경유로 infra 통신
- [ ] `app:api` Controller: `infra:*` 직접 import 제거

### Domain — 불변 객체 & 타입 선택

- [ ] 도메인 객체: setter 제거. 상태 변경 = 새 인스턴스 반환 (`with*/activate/deactivate/modify*`)
- [ ] AR (Aggregate Root): `class` 타입으로 선언
- [ ] VO (행위 있는 불변): `final class` 타입으로 선언
- [ ] VO (순수 데이터 홀더) / Read Model / Command / Query: `record` 타입으로 선언
- [ ] `record`에 도메인 행위(비즈니스 검증, 계산) 제거 — 행위 있으면 `final class`로 전환
- [ ] AR 간 참조: `long` ID-only 방식으로 선언
- [ ] 외부 생성자(`new`) 제거 → static factory 메서드 사용

### Service — 트랜잭션 & 주입

- [ ] `@Component`(Implement Layer): `@Transactional` 제거 — 트랜잭션은 `@Service`만 소유
- [ ] Business Layer (`@Service`): DB 쓰기 포함 메서드에 `@Transactional` 선언
- [ ] Business Layer (`@Service`): 단순 읽기 위임 / `@Cacheable` 메서드의 `@Transactional` 제거
- [ ] `@Cacheable`과 `@Transactional`을 같은 메서드에 병용 제거 (분리 필수)
- [ ] `Reader` 클래스: 쓰기(save/update/delete) 메서드 제거 → `Updater`/`Manager`/`Processor`로 분리

### Error Handling — 예외 계층

- [ ] 도메인 비즈니스 규칙 위반: `CoreDomainException` 하위 BC별 클래스 + static factory 사용
- [ ] 비즈니스 규칙 위반에는 `CoreDomainException` 하위 클래스 사용 (`IllegalStateException` 대체 제거)
- [ ] `CoreDomainException` 직접 throw 제거 → BC별 하위 클래스 사용
- [ ] 신규 예외 클래스: `CoreDomainException` 또는 `CoreApiException` 상속 (`Exception`/`RuntimeException` 직접 상속 제거)
- [ ] API 레이어: `CoreApiException` 사용. 도메인에서 `CoreApiException` throw 제거
- [ ] `BUSINESS` 레벨 예외 (정상 거절): WARN 로그 설정
- [ ] `IMPLEMENTATION` 레벨 예외 (버그 가능성): ERROR 로그 + Alert 설정

### Testing — 작성 기준

- [ ] 도메인 정책 테스트: 태그 없음, 순수 Java로 실행 (Spring 컨텍스트 로드 제거)
- [ ] 통합 테스트: `@Tag("develop")` + `extends DevelopTest`
- [ ] 인수 테스트 (@AcceptanceTest): `@Tag("context")` + `extends ContextTest`
- [ ] 메서드명: `테스트대상_상태_기대결과` 형식 (`validate_RequiredTermNotAgreed_ThrowsException`)
- [ ] `@DisplayName`: 완전한 한글 문장 ("~할 때, ~하면, ~한다/된다")
- [ ] given / when / then 주석 구분
- [ ] `static` 필드를 통한 테스트 간 상태 공유 제거
- [ ] `Thread.sleep()` 제거
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