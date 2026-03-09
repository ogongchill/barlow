# Testing Rules

## 1. 좋은 테스트의 기준

### FIRST 원칙
- **Fast**: 빠르게 실행되어 자주 돌릴 수 있어야 한다.
- **Independent**: 테스트 간 순서·상태 의존 없이 독립적으로 실행 가능해야 한다.
- **Repeatable**: 어느 환경에서도 동일한 결과를 낸다.
- **Self-Validating**: 성공/실패를 자체적으로 판단한다.
- **Timely**: 테스트 대상 코드와 함께 작성한다.

### Complete + Concise
- **Complete(완전)**: 테스트를 이해하는 데 필요한 모든 정보가 테스트 본문에 있다.
- **Concise(간결)**: 불필요한 정보는 포함하지 않는다.

### 가치 기반 선별 작성
- 모든 코드에 테스트를 작성하지 않는다. **작성 가치를 먼저 판단한다.**
- 시스템의 핵심 기능이 아닌 경우 과감히 테스트에서 배제한다.
- **작성 가치 높음**: 상태 전이 불변 규칙이 있는 도메인 메서드, 비즈니스 정책 검증(Policy), 날짜/버전 계산 로직
- **작성 가치 낮음**: 단순 with 패턴, 데이터 홀더, Reader 위임, 단순 getter

### barlow BC별 테스트 우선순위
| BC | 우선순위 | 근거 |
|---|---|---|
| BillPost | 핵심 | `calculateDeadlineDay()` 등 실질 도메인 로직 |
| Subscribe | 핵심 | `activate()`/`deactivate()` 상태 불변 규칙 |
| Account (Term Sub-BC) | 핵심 | `TermsPolicy.validate()` — 법적 의무 포함 |
| Version | 핵심 | `SemanticVersion.isLessThan()` + `ClientVersionPolicy.evaluate()` — 오판 시 앱 사용 불가 |
| NotificationSetting | 핵심 | `activate()`/`deactivate()` 상태 전이 정책 |
| LegislationAccount | 보조 | `with*()` 단순 필드 교체, 도메인 정책 없음 |
| ExternalAuth | 보조 | 중복 provider 방지 정책 하나 |
| Reaction | 보조 | 반응 저장/조회, 복잡한 불변 규칙 없음 |
| Device | 비핵심 | 단순 필드 교체 |
| NotificationCenter | 비핵심 | 순수 Read Model, 로직 없음 |

---

## 2. Barlow 테스트 분류 체계

### 태그 기준 — CI 포함 여부

| 태그 | CI 실행 | 실행 명령 | 의미 |
|---|---|---|---|
| 태그 없음 | O | `./gradlew unitTest` | 순수 Java 단위 테스트. Spring 컨텍스트 없음 |
| `@Tag("context")` | O | `./gradlew contextTest` | 검증 완료, 배포 전 CI에서 검증하는 통합 테스트 |
| `@Tag("restdocs")` | O | `./gradlew restDocsTest` | REST Docs API 문서 생성 테스트 |
| `@Tag("develop")` | X | `./gradlew developTest` | 로컬 실험·개발 중인 테스트. CI 미포함 |

> **`@Tag("develop")` 운영 원칙**: 로컬에서 이것저것 만들어보고, 검증이 완료되면 `@Tag("context")`로 승격한다.
> CI에서 실행되기 위해서는 반드시 `@Tag("context")`여야 한다.

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

```java
// BAD — 파싱 책임과 정책 평가 책임이 혼재
ClientVersionPolicy.evaluate(String clientVersionString)

// GOOD — 이미 파싱된 객체를 받아 정책 평가 책임만 수행
ClientVersionPolicy.evaluate(SemanticVersion clientVersion)
```

### @Nested 사용 기준
- **단순 정책** → flat하게 작성
- **복잡한 정책** (조건 분기가 많은 경우) → `@Nested` BDD 스타일 (Describe-Context-It)

```java
// 단순 정책 — flat
class TermsPolicyTest {

    @Test
    @DisplayName("필수 약관에 동의하지 않으면 RegistrationException이 발생한다.")
    void validate_RequiredTermNotAgreed_ThrowsRegistrationException() {
        // given
        List<Term> activeTerms = List.of(
            new Term(1L, "서비스 이용약관", "1.0", "url", Term.Type.SERVICE, true, LocalDateTime.now())
        );
        TermsPolicy policy = TermsPolicy.from(activeTerms);
        List<TermAgreement> agreements = List.of(
            TermAgreement.disagreedAt(1L, LocalDateTime.now())
        );

        // when & then
        assertThatThrownBy(() -> policy.validate(agreements))
            .isInstanceOf(RegistrationException.class);
    }

    @Test
    @DisplayName("모든 필수 약관에 동의하면 검증을 통과한다.")
    void validate_AllRequiredTermsAgreed_NoException() {
        // given
        List<Term> activeTerms = List.of(
            new Term(1L, "서비스 이용약관", "1.0", "url", Term.Type.SERVICE, true, LocalDateTime.now())
        );
        TermsPolicy policy = TermsPolicy.from(activeTerms);
        List<TermAgreement> agreements = List.of(TermAgreement.agreedAt(1L, LocalDateTime.now()));

        // when & then
        assertThatCode(() -> policy.validate(agreements)).doesNotThrowAnyException();
    }
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
            void deactivate_ActiveSubscription_ReturnsFalseIsActive() {
                Subscription active = new Subscription(1L, 10L, LegislationType.EDUCATION, true);
                Subscription deactivated = active.deactivate();
                assertThat(deactivated.isActive()).isFalse();
            }
        }

        @Nested
        @DisplayName("이미 구독 취소 상태일 때")
        class WhenAlreadyInactive {

            @Test
            @DisplayName("구독을 취소하면 SubscriptionDomainException이 발생한다.")
            void deactivate_InactiveSubscription_ThrowsSubscriptionDomainException() {
                Subscription inactive = new Subscription(1L, 10L, LegislationType.EDUCATION, false);
                assertThatThrownBy(inactive::deactivate)
                    .isInstanceOf(SubscriptionDomainException.class);
            }
        }
    }
}
```

---

## 4. 유스케이스 테스트 (인수 테스트)

**목적:** 사용자 여정(user journey)이 의도한 대로 이루어지는지 보장.
인수 조건에 따라 소프트웨어가 올바르게 동작하는지 검증하며, 테스트가 문서 역할을 한다.

- **`@AcceptanceTest`** 애노테이션 사용 (`@Tag("context")` 포함 — CI 실행)
- **RestAssured** 로 HTTP 레벨부터 실제 호출
- **블랙박스 테스트** — API 명세만 알고 세부 구현은 모른다. 비즈니스 변경이 아니면 테스트가 실패하지 않는다.
- **DTO 대신 `Map`** 사용 — 블랙박스 원칙을 위한 의도된 선택

### `@AcceptanceTest` 구조

```java
// 애노테이션 정의 (이미 구현됨)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
                classes = BarlowCoreApiApplication.class)
@TestExecutionListeners(value = {AcceptanceTestExecutionListener.class},
                        mergeMode = MERGE_WITH_DEFAULTS)
public @interface AcceptanceTest {
    String[] setUpScripts() default {};  // JSON 기반 테스트 데이터
}

// 사용 예시
@AcceptanceTest({"acceptance/user.json", "acceptance/device.json", "acceptance/term.json"})
class AccountControllerTest extends ContextTest {

    @Autowired
    private TestTokenProvider testTokenProvider;

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
// acceptance/user.json 예시
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

- **`@Tag("restdocs")`** (CI 미포함 — `api-docs.yml` 워크플로우에서 develop 머지 시 자동 실행)
- 실행 명령: `./gradlew :app:api:restDocsTest` → `:app:api:asciidoctor`
- 결과물: `app/api/build/docs/asciidoc/api-docs.html` → `https://ogongchill.github.io/barlow/api/` 자동 배포

### `RestDocsContextTest` 기반 클래스

```java
// 이미 구현됨 — com.barlow.app.support.RestDocsContextTest
@Tag("restdocs")
@ExtendWith(RestDocumentationExtension.class)
public abstract class RestDocsContextTest extends ContextTest {

    protected RequestSpecification spec;

    @BeforeEach
    void setUpRestDocs(RestDocumentationContextProvider provider) {
        this.spec = new RequestSpecBuilder()
            .addFilter(RestAssuredRestDocumentation.documentationConfiguration(provider))
            .build();
    }
}
```

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

### 사용 예시

```java
@AcceptanceTest({"acceptance/user.json", "acceptance/device.json"})
@Import(TestTokenProvider.class)
class AccountControllerDocsTest extends RestDocsContextTest {

    @Autowired
    private TestTokenProvider testTokenProvider;

    @Test
    @DisplayName("내 계정 조회 API 문서화")
    void getMyAccount() {
        RestAssured.given(spec)
            .filter(document("account/get-my",
                RestDocUtils.requestPreprocessor(),
                RestDocUtils.responsePreprocessor(),
                requestHeaders(
                    headerWithName(AUTHORIZATION).description("Bearer 액세스 토큰")
                ),
                relaxedResponseFields(
                    fieldWithPath("result").description("결과 상태 (SUCCESS)"),
                    fieldWithPath("data.user.userNo").description("사용자 번호"),
                    fieldWithPath("data.user.role").description("사용자 역할 (GUEST / MEMBER)"),
                    subsectionWithPath("data.devices").description("연결된 기기 목록")
                )
            ))
            .header(AUTHORIZATION, AUTHENTICATION_TYPE + testTokenProvider.getAccessTokenValue())
            .when().get("/api/v1/account/my")
            .then().statusCode(200);
    }
}
```

### 문서화 스니펫 종류

| 스니펫 메서드 | 대상 |
|---|---|
| `requestHeaders()` | 요청 헤더 (Authorization, X-Client-OS 등) |
| `pathParameters()` | 경로 변수 (`{billId}`, `{legislationType}` 등) |
| `queryParameters()` | 쿼리 파라미터 (`page`, `size`, `targetType` 등) |
| `requestFields()` | 요청 바디 필드 |
| `relaxedResponseFields()` | 응답 필드 (미기술 필드 무시) |

### 외부 서비스 대역 처리

OIDC 인증 등 외부 서비스가 필요한 DocsTest는 AcceptanceTest와 동일하게 `FakeOidcAuthenticationService`를 `@Import`하여 사용한다.

```java
@AcceptanceTest({"acceptance/user.json", ...})
@Import(FakeOidcAuthenticationService.class)
class AuthControllerDocsTest extends RestDocsContextTest {
    @Autowired FakeOidcAuthenticationService fakeOidcService;

    @BeforeEach void setUpFake() { fakeOidcService.willReturn(...); }
    @AfterEach  void resetFake() { fakeOidcService.reset(); }
}
```

---

## 6. 기타 테스트

### 부분 기능 테스트

전체 테스트 작성이 어렵거나 비용 대비 가치가 낮을 때, 핵심 부분 기능만 테스트한다.
`@VisibleForTesting`으로 접근 제어를 완화하여 내부 로직을 직접 테스트할 수 있다.

```java
@VisibleForTesting
int calculateEventBonus(int amount) { ... }
```

> **주의**: 비즈니스 생명 주기가 짧거나 국소적인 기능에만 적용한다. 남용 시 캡슐화가 훼손된다.

### 학습 테스트

비즈니스 로직이 아니라 **특정 도구나 라이브러리의 동작을 학습**하기 위한 테스트.

- `@Tag("develop")` 로 작성 — CI 미포함
- 학습 목적이 달성되면 삭제 또는 유지 (팀 판단)

```java
@Tag("develop")
class CaffeineLearnTest {

    @Test
    @DisplayName("Caffeine 캐시에서 만료된 항목은 다음 접근 시 evict된다.")
    void caffeine_expiry_behavior() { ... }
}
```

---

## 7. 테스트 구조 규칙

### 메서드 네이밍: `테스트대상_상태_기대결과`

```java
// GOOD
void validate_RequiredTermNotAgreed_ThrowsRegistrationException()
void activate_AlreadyActive_ThrowsSubscriptionDomainException()
void deactivate_InactiveSubscription_ReturnsDeactivatedSubscription()

// BAD
void testValidate()           // 정보 없음
void 약관검증_성공()           // 영문 메서드명 규칙 불일치
void validate()               // 기대 결과 없음
```

### @DisplayName: 비즈니스 명세 문장

완전한 한글 문장. "~할 때, ~하면, ~하다/된다" 형태.

```java
// GOOD
@DisplayName("필수 약관에 동의하지 않으면 RegistrationException이 발생한다.")
@DisplayName("이미 구독 취소한 입법계정을 다시 취소하면 SubscriptionDomainException이 발생한다.")
@DisplayName("구독 중인 입법계정을 구독 취소하면 isActive()가 false가 된다.")

// BAD
@DisplayName("구독 테스트")           // 너무 추상적
@DisplayName("예외 발생")             // 결과만, 조건 없음
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

### 종류 정의

| 종류 | 정의 | 사용 시점 |
|---|---|---|
| **Dummy** | 전달되지만 실제로 사용되지 않음. 매개변수 목록을 채우는 데만 사용 | 비즈니스 외적 부수효과 (알림 발송 등) |
| **Stub** | 사전에 반환값을 지정하여 호출 시 준비된 답변 제공 | 데이터 확보가 어려운 내부 서비스 |
| **Spy** | 실제 객체의 동작을 유지하면서 호출 여부·횟수를 추적 | 상호작용 검증이 필요한 경우 |
| **Mock** | 멤버 함수 호출을 기록하고 상호작용을 검증 | 부수 효과를 일으키는 객체 |
| **Fake** | 실제 구현과 유사하게 동작하지만 프로덕션엔 부적합한 객체 | 외부 서비스 (FCM, 외부 API 등) |

### 도메인 정책 테스트
- **실제 객체 우선** — Mock 프레임워크 사용 최소화
- **VO는 무조건 실제 객체** — `SemanticVersion`, `ExternalPrincipal` 등 Mock 금지
- 협력 객체가 단순한 VO/record라면 직접 생성

### 인수 테스트 (AcceptanceTest)

| 상황 | 대역 종류 | 예시 |
|---|---|---|
| 외부 서비스 (FCM, 외부 API) | **Fake** — 실제와 유사한 구현체 | `MockFcmClient` |
| 비즈니스 외적 부수효과 (알림·Slack 등) | **Dummy** — 아무것도 안 하는 구현체 | `NoopAlerter` |
| 데이터 확보 어려운 내부 서비스 | **Stub** — `given(...).willReturn(...)` | 캐시 응답 |
| 그 외 | **실제 객체** | Repository, Service |

> **`@MockBean` / `@SpyBean` 주의**: 테스트 컨텍스트 캐시 키를 변경하여 새 컨텍스트를 띄운다. 불필요한 사용을 피한다.

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
// BAD — 비즈니스 레이어를 Mock으로 단위 테스트 (AcceptanceTest로 대체)
@Mock
private AccountCreateService accountCreateService;

// BAD — Repository 테스트 별도 작성 (AcceptanceTest가 DB까지 커버)
@Tag("context")
class SubscriptionRepositoryAdapterTest { ... }

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

// BAD — VO를 Mock으로 대체 (실제 객체 사용 원칙 위반)
SemanticVersion mockVersion = mock(SemanticVersion.class);

// BAD — 테스트 경계 모호함을 Mock으로 해결 (설계 리팩토링으로 해결해야 함)
given(mockSemanticVersion.isLessThan(any())).willReturn(true);
```