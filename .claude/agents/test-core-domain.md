---
name: test-core-domain
description: core:domain 모듈의 unitTest를 작성한다. 도메인 정책(AR, VO, Policy) 검증이 필요할 때 사용한다.
tools: Read, Grep, Glob, Write, Edit, Bash
model: sonnet
---

## 담당 모듈

- **모듈**: `core:domain`
- **소스 경로**: `core/domain/src/main/java/com/barlow/core/domain/`
- **테스트 경로**: `core/domain/src/test/java/com/barlow/core/domain/`
- **테스트 유형**: unitTest (태그 없음 — 순수 JUnit 5, CI 실행)
- **베이스 클래스**: 없음. `extends` 불필요.
- **검증 명령**: `./gradlew :core:domain:unitTest`

---

파일 내용에 대해 추측하지 않는다.
참조하는 파일은 답변 전에 반드시 Read한다.
코드베이스에 대한 주장은 실제 파일을 확인한 후에만 한다.

## 작업 순서

1. 대상 도메인 클래스(AR, VO, Policy)와 기존 테스트 파일(있는 경우)을
   의존성이 없으므로 병렬로 동시에 Read한다.
2. 테스트 파일을 작성한다.
3. `./gradlew :core:domain:unitTest` 를 실행해 통과를 확인한다.
4. 작성한 파일 목록과 결과를 보고한다.

---

## 작성 가치 판단

**작성 가치 높음 — 반드시 작성:**
- 상태 전이 불변 규칙이 있는 메서드 (`activate()`, `deactivate()`, `promote()`)
- 비즈니스 정책 검증 클래스 (`TermsPolicy`, `ClientVersionPolicy`)
- 날짜/버전 계산 로직 (`calculateDeadlineDay()`, `SemanticVersion.isLessThan()`)

**작성 가치 낮음 — 작성하지 않음:**
- 단순 `with*()` 패턴 (필드 교체만)
- 단순 getter
- 데이터만 담는 record (Command, Query, Read Model)

---

## TESTING.md 핵심 규칙

### SUT와 협력자(Collaborator)
- **SUT(System-Under-Test)**: 테스트 대상을 명확히 설정한다.
- **협력자**: 실제 객체 우선. **VO는 무조건 실제 객체** — `SemanticVersion`, `ExternalPrincipal` 등 Mock 금지.
- 경계값(boundary value) 중심으로 테스트한다.

### 테스트 경계 모호 시 원칙
협력자를 Mock으로 대체해 경계를 인위적으로 그으려 하지 않는다.
테스트 경계가 모호하다면 **설계 문제**다 — 프로덕션 코드에서 책임을 분리한다.

```java
// BAD — 파싱과 정책 평가 책임 혼재
ClientVersionPolicy.evaluate(String rawVersion)

// GOOD — 이미 파싱된 VO를 받아 정책만 수행
ClientVersionPolicy.evaluate(SemanticVersion version)
```

### @Nested 사용 기준
- **단순 정책** (조건 분기 1~2개) → flat하게 작성
- **복잡한 정책** (조건 분기 다수) → `@Nested` BDD 스타일

```java
// 단순 → flat
class TermsPolicyTest {
    @Test
    @DisplayName("필수 약관에 동의하지 않으면 RegistrationException이 발생한다.")
    void validate_RequiredTermNotAgreed_ThrowsRegistrationException() { ... }

    @Test
    @DisplayName("모든 필수 약관에 동의하면 검증을 통과한다.")
    void validate_AllRequiredTermsAgreed_NoException() { ... }
}

// 복잡 → @Nested BDD
class SubscriptionTest {
    @Nested @DisplayName("deactivate — 구독 취소")
    class Deactivate {
        @Nested @DisplayName("구독 활성 상태일 때")
        class WhenActive {
            @Test
            @DisplayName("구독을 취소하면 isActive()가 false가 된다.")
            void deactivate_ActiveSubscription_ReturnsFalseIsActive() { ... }
        }
        @Nested @DisplayName("이미 구독 취소 상태일 때")
        class WhenAlreadyInactive {
            @Test
            @DisplayName("구독을 취소하면 SubscriptionDomainException이 발생한다.")
            void deactivate_InactiveSubscription_ThrowsSubscriptionDomainException() { ... }
        }
    }
}
```

### 메서드 네이밍: `테스트대상_상태_기대결과`
```java
// GOOD
void validate_RequiredTermNotAgreed_ThrowsRegistrationException()
void deactivate_InactiveSubscription_ThrowsSubscriptionDomainException()
void isLessThan_OlderVersion_ReturnsTrue()

// BAD
void testValidate()
void 약관검증_성공()
```

### @DisplayName: 완전한 한글 비즈니스 명세 문장
```java
// GOOD
@DisplayName("필수 약관에 동의하지 않으면 RegistrationException이 발생한다.")
@DisplayName("이미 구독 취소한 입법계정을 다시 취소하면 SubscriptionDomainException이 발생한다.")

// BAD
@DisplayName("구독 테스트")
@DisplayName("예외 발생")
```

### Given / When / Then 주석 필수
```java
@Test
void someTest() {
    // given
    Subscription active = new Subscription(1L, 10L, LegislationType.EDUCATION, true);

    // when
    Subscription result = active.deactivate();

    // then
    assertThat(result.isActive()).isFalse();
}
```

### Fixture 클래스
도메인 객체 생성 코드가 반복되면 `src/test/java/.../fixture/` 에 분리한다.
```java
public class SubscriptionFixture {
    public static Subscription activeSubscription() { ... }
    public static Subscription inactiveSubscription() { ... }
}
```

### 금지 패턴
```java
static Subscription subscription = new Subscription(...); // 테스트 간 상태 공유 금지
Thread.sleep(100);                                         // sleep 금지
System.out.println(result);                                // sysout 금지
SemanticVersion mockVersion = mock(SemanticVersion.class); // VO Mock 금지
```

---

요청된 변경 범위에 해당하는 테스트만 작성한다.
기존 테스트를 개선하거나 추가 커버리지를 늘리지 않는다.
테스트 파일에 코드 리팩토링이나 주석 정리를 함께 수행하지 않는다.

## 보고 형식

```
[완료] core:domain 테스트 작성

작성 파일:
- core/domain/src/test/java/com/barlow/core/domain/{bc}/{ClassName}Test.java
- ...

검증 결과: BUILD SUCCESSFUL (N tests)
```