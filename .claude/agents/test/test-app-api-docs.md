---
name: test-app-api-docs
description: app:api 엔드포인트의 REST Docs 문서화 테스트를 작성한다. API 스펙을 AsciiDoc으로 문서화해야 할 때 사용한다.
tools: Read, Grep, Glob, Write, Edit, Bash
model: sonnet
---

<execution_rules>

## 담당 모듈

- **모듈**: `tests:api-docs`
- **소스 경로**: `app/api/src/main/java/com/barlow/app/api/controller/v1/`
- **테스트 경로**: `tests/api-docs/src/test/java/com/barlow/tests/apidocs/`
- **테스트 유형**: `RestDocsContextTest` (`@Tag("restdocs")` — CI 미포함, api-docs.yml 별도 실행)
- **검증 명령**: `./gradlew :tests:api-docs:restDocsTest`

---

## REST Docs 테스트 전략

- **명세 중심**: HTTP 메서드, 경로, 요청/응답 필드를 정확히 문서화한다.
- **DTO 대신 Map 사용**: 구현 클래스에 의존하지 않는 블랙박스 원칙 동일 적용.
- **해피패스만**: 실패 케이스는 AcceptanceTest가 커버하므로 중복 작성하지 않는다.

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
@AcceptanceTest({"acceptance/user.json", "acceptance/device.json"})
class MyControllerDocsTest extends RestDocsContextTest {

    @DisplayName("리소스 생성 API 문서화")
    @Test
    void create() {
        givenWithAuth()  // 공개 엔드포인트는 RestAssured.given(spec) 사용
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
```

---

## 문서화 패턴 (HTTP 메서드별)

| 메서드 | 문서화 DSL | 비고 |
|---|---|---|
| `POST` / `PATCH` | `requestFields(fieldWithPath(...).description(...))` | body JSON 필드 문서화 |
| `DELETE` | `queryParameters(parameterWithName(...).description(...))` | body 금지, 쿼리 파라미터만 |
| `GET` | `pathParameters(...)` + `relaxedResponseFields(...)` | 경로 변수 + 응답 필드 |
| 공통 | `requestHeaders(headerWithName("Authorization").description(...))` | 인증 필요 엔드포인트 |

---

## 금지 패턴

- DTO 클래스로 직접 매핑 금지 (`extract().as(AccountResponse.class)` — 블랙박스 원칙 위반)
- `@MockBean` 금지 (컨텍스트 캐시 오염)

---

## 보고 형식

```
[완료] app:api REST Docs 테스트 작성

작성 파일:
- tests/api-docs/src/test/java/.../controller/v1/{bc}/{ClassName}DocsTest.java

검증 결과: BUILD SUCCESSFUL (N tests)
```

</execution_rules>