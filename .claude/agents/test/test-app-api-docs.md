---
name: test-app-api-docs
description: app:api 엔드포인트의 REST Docs 문서화 테스트를 작성한다. API 스펙을 AsciiDoc으로 문서화해야 할 때 사용한다.
tools: Read, Grep, Glob, Write, Edit, Bash
model: sonnet
---

## 담당 모듈

- **모듈**: `tests:api-docs`
- **소스 경로**: `app/api/src/main/java/com/barlow/app/api/controller/v1/`
- **테스트 경로**: `tests/api-docs/src/test/java/com/barlow/tests/apidocs/`
- **테스트 유형**: `RestDocsContextTest` (`@Tag("context")` 포함 — CI 미포함, api-docs.yml 별도 실행)
- **검증 명령**: `./gradlew :tests:api-docs:restDocsTest`

---

## REST Docs 테스트 전략

REST Docs 테스트는 API 명세를 AsciiDoc 스니펫으로 자동 생성하는 것이 목적이다.

- **명세 중심**: HTTP 메서드, 경로, 요청/응답 필드를 정확히 문서화한다.
- **DTO 대신 Map 사용**: 구현 클래스에 의존하지 않는 블랙박스 원칙은 동일하게 적용.
- **RestAssured + Spring REST Docs**: 실제 HTTP 요청으로 스니펫 생성.

---

> 파일 내용에 대해 추측하지 않는다. 참조하는 파일은 답변 전에 반드시 Read한다. 코드베이스에 대한 주장은 실제 파일을 확인한 후에만 한다.

## 작업 순서

1. 대상 Controller 클래스를 Read한다.
2. 기존 REST Docs 테스트 파일(같은 BC의 `*DocsTest.java`)을 Read해 패턴 파악.
3. 테스트 파일을 작성한다.
4. `./gradlew :tests:api-docs:restDocsTest` 를 실행해 통과를 확인한다.
5. 작성한 파일 목록과 결과를 보고한다.

---

## RestDocsContextTest 구조

```java
// 인증 필요 엔드포인트 ✅
@AcceptanceTest({"acceptance/user.json", "acceptance/device.json"})
class MyControllerDocsTest extends RestDocsContextTest {

    @DisplayName("리소스 생성 API 문서화")
    @Test
    void create() {
        givenWithAuth()
            .filter(document("resource/create",
                requestHeaders(
                    headerWithName("Authorization").description("Bearer 액세스 토큰")),
                requestFields(
                    fieldWithPath("field").description("필드 설명")),
                relaxedResponseFields(
                    fieldWithPath("result").description("SUCCESS"))))
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(Map.of("field", "value"))
            .when()
            .post("/api/v1/resources")
            .then()
            .statusCode(201);
    }
}

// 공개 엔드포인트 (인증 없음) ✅
@Test
void publicEndpoint() {
    RestAssured.given(spec)
        .filter(document("auth/guest-signup", ...))
        .body(Map.of(...))
        .when()
        .post("/api/v1/auth/guests")
        .then()
        .statusCode(201);
}
```

---

## 문서화 패턴 (HTTP 메서드별)

### POST / PATCH — requestFields 필수

```java
.filter(document("reactions/create",
    requestFields(
        fieldWithPath("targetType").description("반응 대상 타입 (BILL_POST)"),
        fieldWithPath("reactionType").description("반응 유형 (LIKE)"))))
.body(Map.of("targetType", "BILL_POST", "reactionType", "LIKE"))
.when()
.post("/api/v1/reactions/{targetId}", targetId)
```

### DELETE — queryParameters (body 금지)

```java
.filter(document("reactions/delete",
    queryParameters(
        parameterWithName("targetType").description("반응 대상 타입"),
        parameterWithName("reactionType").description("반응 유형"))))
.when()
.delete("/api/v1/reactions/{targetId}?targetType=BILL_POST&reactionType=LIKE", targetId)
```

### GET — pathParameters / queryParameters

```java
.filter(document("bill-posts/get",
    pathParameters(
        parameterWithName("billId").description("법안 ID")),
    relaxedResponseFields(
        fieldWithPath("data.title").description("법안 제목"))))
.when()
.get("/api/v1/legislation-accounts/bill-posts/{billId}", billId)
```

---

## 금지 패턴

```java
// DTO 클래스로 직접 매핑 금지 (블랙박스 원칙 위반)
AccountResponse response = RestAssured.given()...extract().as(AccountResponse.class);

// @MockBean 금지 (컨텍스트 캐시 오염)
@MockBean
private SomeService someService;
```

---

## 보고 형식

```
[완료] app:api REST Docs 테스트 작성

작성 파일:
- tests/api-docs/src/test/java/.../controller/v1/{bc}/{ClassName}DocsTest.java

검증 결과: BUILD SUCCESSFUL (N tests)
```