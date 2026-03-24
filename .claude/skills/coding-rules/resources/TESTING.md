# Testing Rules

## 1. 테스트 작성 기준

### 가치 기반 선별 작성
- 모든 코드에 테스트를 작성하지 않는다. **작성 가치를 먼저 판단한다.**
- 시스템의 핵심 기능이 아닌 경우 과감히 테스트에서 배제한다.
- **작성 가치 높음**: 상태 전이 불변 규칙이 있는 도메인 메서드, 비즈니스 정책 검증(Policy), 날짜/버전 계산 로직
- **작성 가치 낮음**: 단순 with 패턴, 데이터 홀더, Reader 위임, 단순 getter

---

## 2. Barlow 테스트 분류 체계

### 태그 기준 — CI 포함 여부

| 태그 | CI 실행 | 실행 명령 | 의미 |
|---|---|---|---|
| 태그 없음 | O | `./gradlew unitTest` | 순수 Java 단위 테스트. Spring 컨텍스트 없음 |
| `@Tag("context")` | O | `./gradlew contextTest` | 검증 완료, 배포 전 CI에서 검증하는 통합 테스트 |
| `@Tag("restdocs")` | O | `./gradlew restDocsTest` | REST Docs API 문서 생성 테스트 |
| `@Tag("develop")` | X | `./gradlew developTest` | 로컬 실험·개발 중인 테스트. 검증 완료 시 `@Tag("context")`로 승격한다. |

### 비즈니스 레이어 테스트 전략
**비즈니스 레이어(`@Service`, 유스케이스 클래스)는 Mock 단위 테스트로 검증하지 않는다.**
`@AcceptanceTest`(인수 테스트)가 HTTP 레벨부터 DB까지 end-to-end로 한 번에 검증한다.
Repository 테스트도 별도로 작성하지 않는다 — AcceptanceTest가 DB까지 커버한다.

---

## 3. 도메인 정책 테스트

**목적:** 도메인 정책이 올바른지 빠르게 검증. 테스트 자체가 비즈니스 명세 문서가 된다.

- **태그 없음** (unitTest로 실행)
- **Spring 컨텍스트 없이** 순수 Java로 작성
- **SUT(System-Under-Test)** 를 명확히 설정하고 **경계값(boundary value)** 중심으로 테스트한다.
- **협력자(Collaborator)** 는 실제 객체 우선. Mock 프레임워크 최소화.
- **VO는 무조건 실제 객체** — `SemanticVersion`, `ExternalPrincipal` 등은 Mock 금지.

### 테스트 경계 모호 시 원칙
협력자를 Mocking하여 경계를 인위적으로 그으려 하지 않는다.
테스트 경계가 모호하다면 이는 **설계 문제의 신호**다 — 책임을 분리하도록 프로덕션 코드를 리팩토링한다.

### @Nested 사용 기준
- **단순 정책** → flat하게 작성
- **복잡한 정책** (조건 분기가 많은 경우) → `@Nested` BDD 스타일 (Describe-Context-It)

```java
// 단순 정책 — flat
class TermsPolicyTest {

    @Test
    @DisplayName("필수 약관에 동의하지 않으면 RegistrationException이 발생한다.")
    void validate_RequiredTermNotAgreed_ThrowsRegistrationException() { ... }

    @Test
    @DisplayName("모든 필수 약관에 동의하면 검증을 통과한다.")
    void validate_AllRequiredTermsAgreed_NoException() { ... }
}

// 복잡한 정책 — @Nested BDD
class SubscriptionTest {

    @Nested
    @DisplayName("deactivate — 구독 취소")
    class Deactivate {

        @Nested
        @DisplayName("구독 활성 상태일 때")
        class WhenActive {
            @Test
            @DisplayName("구독을 취소하면 isActive()가 false가 된다.")
            void deactivate_ActiveSubscription_ReturnsFalseIsActive() { ... }
        }

        @Nested
        @DisplayName("이미 구독 취소 상태일 때")
        class WhenAlreadyInactive {
            @Test
            @DisplayName("구독을 취소하면 SubscriptionDomainException이 발생한다.")
            void deactivate_InactiveSubscription_ThrowsSubscriptionDomainException() { ... }
        }
    }
}
```

---

## 4. 유스케이스 테스트 (인수 테스트)

**목적:** 사용자 여정(user journey)이 의도한 대로 이루어지는지 보장.

- **`@AcceptanceTest`** 애노테이션 사용 (`@Tag("context")` 포함 — CI 실행)
- **RestAssured** 로 HTTP 레벨부터 실제 호출
- **블랙박스 테스트** — API 명세만 알고 세부 구현은 모른다. 비즈니스 변경이 아니면 테스트가 실패하지 않는다.
- **DTO 대신 `Map`** 사용 — 블랙박스 원칙을 위한 의도된 선택

### 사용 예시

```java
@AcceptanceTest({"acceptance/user.json", "acceptance/device.json", "acceptance/term.json"})
class AccountControllerTest extends ContextTest {

    @Test
    @DisplayName("GUEST 사용자가 탈퇴하면 사용자와 디바이스 정보가 삭제된다.")
    void withdraw_guest() {
        // given
        String targetDeviceId = "device_id_1";

        // when
        Map<String, Object> responseMap = RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .headers(AUTHORIZATION, AUTHENTICATION_TYPE + testTokenProvider.getAccessTokenValue())
            .headers(X_DEVICE_ID, targetDeviceId)
            .when().post("/api/v1/account/withdraw")
            .then().log().all().extract().jsonPath().getMap(".");

        // then - API 응답 검증
        assertThat(responseMap).containsEntry("result", ResultType.SUCCESS.name());

        // then - DB 상태 검증 (JdbcTemplate으로 직접 확인)
        boolean userExists = jdbcTemplate.queryForObject(
            "SELECT EXISTS (SELECT 1 FROM barlow_user WHERE no = ?)", Integer.class, 1L) == 1;
        assertThat(userExists).isFalse();
    }
}
```

### 테스트 데이터 관리 (`setUpScripts`)

`AcceptanceTestExecutionListener`가 각 테스트 메서드 전후로 데이터를 관리한다:
- **beforeTestMethod**: `setUpScripts`에 지정된 JSON 파일로 DB에 INSERT
- **afterTestMethod**: 전체 테이블 TRUNCATE (테스트 격리)

```json
{
  "barlow_user": [
    { "no": 1, "nickname": "nickname", "role": "GUEST" },
    { "no": 2, "nickname": "existing_member", "role": "MEMBER" }
  ]
}
```

### 테스트 컨텍스트 재사용

`@AcceptanceTest`는 Spring 컨텍스트 캐싱을 의도하고 있다.
아래 항목은 컨텍스트 캐시 키를 변경하므로 주의한다:
- `@MockBean` / `@SpyBean` 사용
- `@ActiveProfiles` 추가/변경
- `@TestPropertySource` 변경

### 거짓 양성(False Positive) 대응

반환값이 없는 유스케이스는 중간에 종료되어도 테스트가 성공으로 판단될 수 있다.
**프로덕션 코드에 응답값을 추가하여** 검증 가능하게 만드는 것을 권장한다.
테스트는 제품의 첫 번째 고객이므로, 테스트를 위해 프로덕션 코드를 변경하는 것은 정당하다.

---

## 5. API 문서화 테스트 (RestDocsContextTest)

**목적:** API 스펙(요청/응답 구조, 파라미터, 헤더)을 실제 HTTP 호출 결과를 기반으로 문서화한다.
AcceptanceTest가 사용자 여정(블랙박스)을 검증한다면, DocsTest는 API 계약(화이트박스 필드 설명)을 기록한다.

- **`@Tag("restdocs")`** — `api-docs.yml` 워크플로우에서 develop 머지 시 자동 실행
- 실행 명령: `./gradlew :app:api:restDocsTest` → `:app:api:asciidoctor`
- 결과물: `app/api/build/docs/asciidoc/api-docs.html` → `https://ogongchill.github.io/barlow/api/` 자동 배포
- 기반 클래스: `RestDocsContextTest extends ContextTest` (이미 구현됨)

### AcceptanceTest vs DocsTest — 역할 분리

| 구분 | AcceptanceTest | DocsTest |
|---|---|---|
| 목적 | 사용자 여정 검증 (블랙박스) | API 스펙 문서화 (화이트박스) |
| 시나리오 범위 | 성공 + 실패 케이스 | **성공(해피패스)만** |
| 필드 설명 | 없음 | requestFields, responseFields 등 |
| 태그 | `@Tag("context")` | `@Tag("restdocs")` |
| CI | 항상 실행 | develop 머지 시 `api-docs.yml`에서만 |

### 작성 규칙

**언제 DocsTest를 작성하는가:**
- `app:api`에 새 Controller 또는 엔드포인트를 추가할 때 반드시 함께 작성한다.

**해피패스만 문서화:**
- 실패 케이스(오류 응답)는 AcceptanceTest가 이미 검증하므로 DocsTest에 중복 작성하지 않는다.

**`relaxedResponseFields()` 사용:**
- 모든 필드를 빠짐없이 기술하기보다, 중요 필드를 선별적으로 기술하고 나머지는 `subsectionWithPath()`로 묶는다.
- 응답 구조가 바뀌어도 DocsTest가 쉽게 깨지지 않게 한다.

### 문서화 스니펫 종류

| 스니펫 메서드 | 대상 |
|---|---|
| `requestHeaders()` | 요청 헤더 (Authorization, X-Client-OS 등) |
| `pathParameters()` | 경로 변수 (`{billId}`, `{legislationType}` 등) |
| `queryParameters()` | 쿼리 파라미터 (`page`, `size`, `targetType` 등) |
| `requestFields()` | 요청 바디 필드 |
| `relaxedResponseFields()` | 응답 필드 (미기술 필드 무시) |

### 외부 서비스 대역 처리

OIDC 인증 등 외부 서비스가 필요한 DocsTest는 `FakeOidcAuthenticationService`를 `@Import`하여 사용한다. (AcceptanceTest와 동일 방식)

---

## 6. 기타 테스트

### 부분 기능 테스트

전체 테스트 작성이 어렵거나 비용 대비 가치가 낮을 때, `@VisibleForTesting`으로 접근 제어를 완화하여 내부 로직을 직접 테스트할 수 있다.

> **주의**: 비즈니스 생명 주기가 짧거나 국소적인 기능에만 적용한다. 남용 시 캡슐화가 훼손된다.

### 학습 테스트

특정 도구나 라이브러리의 동작을 학습하기 위한 테스트. `@Tag("develop")`로 작성하며 CI 미포함. 학습 목적이 달성되면 삭제 또는 유지(팀 판단).

---

## 7. 테스트 구조 규칙

### 메서드 네이밍: `테스트대상_상태_기대결과`

```java
// GOOD
void validate_RequiredTermNotAgreed_ThrowsRegistrationException()
void deactivate_InactiveSubscription_ReturnsDeactivatedSubscription()

// BAD
void testValidate()           // 정보 없음
void validate()               // 기대 결과 없음
```

### @DisplayName: 비즈니스 명세 문장

완전한 한글 문장. "~할 때, ~하면, ~하다/된다" 형태.

```java
// GOOD
@DisplayName("필수 약관에 동의하지 않으면 RegistrationException이 발생한다.")
@DisplayName("구독 중인 입법계정을 구독 취소하면 isActive()가 false가 된다.")

// BAD
@DisplayName("구독 테스트")           // 너무 추상적
@DisplayName("subscription test")    // 영문 + 불완전
```

### Given / When / Then 주석

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

---

## 8. 테스트 대역 (Test Double) 사용 기준

### 인수 테스트 (AcceptanceTest)

| 상황 | 대역 종류 | 예시 |
|---|---|---|
| 외부 서비스 (FCM, 외부 API) | **Fake** — 실제와 유사한 구현체 | `MockFcmClient` |
| 비즈니스 외적 부수효과 (알림·Slack 등) | **Dummy** — 아무것도 안 하는 구현체 | `NoopAlerter` |
| 데이터 확보 어려운 내부 서비스 | **Stub** — `given(...).willReturn(...)` | 캐시 응답 |
| 그 외 | **실제 객체** | Repository, Service |

---

## 9. Fixture 클래스

도메인 객체 생성 코드는 `src/test/java/.../fixture` 패키지에 분리한다.

```
src/test/java/com/barlow/{module}/
  └── fixture/
       ├── SubscriptionFixture.java
       ├── BillPostFixture.java
       └── UserFixture.java
```

**Fixture 작성 규칙:**
- 도메인 상태 전이 순서를 반드시 따른다.
- 도메인 팩토리 메서드를 통해 생성한다. 생성자 직접 호출 최소화.

```java
public class SubscriptionFixture {

    public static Subscription activeSubscription() {
        return new Subscription(1L, 10L, LegislationType.EDUCATION, true);
    }

    public static Subscription inactiveSubscription() {
        return new Subscription(1L, 10L, LegislationType.EDUCATION, false);
    }
}
```

---

## 10. 금지 패턴

```java
// BAD — 테스트 간 상태 공유 (Independent 위반)
static Subscription subscription = new Subscription(...);

// BAD — Thread.sleep 사용 (Repeatable 위반)
Thread.sleep(100);

// BAD — 테스트 메서드 내 System.out.println (Self-Validating 위반)
System.out.println(result);

// BAD — 단일 테스트에 여러 시나리오 혼재 (Concise 위반)
void testAll() {
    // 구독 활성화 시나리오
    // 구독 취소 시나리오
    // 예외 시나리오
    // 모두 한 메서드에 ...
}
```