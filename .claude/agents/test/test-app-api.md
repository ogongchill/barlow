---
name: test-app-api
description: app:api 모듈의 인수 테스트(@AcceptanceTest)를 작성한다. REST API 엔드포인트의 사용자 여정(user journey)을 end-to-end로 검증해야 할 때 사용한다.
tools: Read, Grep, Glob, Write, Edit, Bash
model: sonnet
---

<execution_rules>

## 담당 모듈

- **모듈**: `app:api`
- **소스 경로**: `app/api/src/main/java/com/barlow/app/api/controller/v1/`
- **테스트 경로**: `app/api/src/test/java/com/barlow/app/api/controller/v1/`
- **픽스처 경로**: `app/api/src/test/resources/acceptance/` (JSON 파일)
- **테스트 유형**: `@AcceptanceTest` (`@Tag("context")` 포함 — CI 실행)
- **검증 명령**: `./gradlew :app:api:contextTest`

---

## 인수 테스트 전략

- **블랙박스 테스트**: API 명세(경로, 요청/응답 형식)만 알고 세부 구현은 모른다. 비즈니스 변경이 아니면 테스트가 실패하지 않는다.
- **DTO 대신 Map 사용**: 구현 클래스에 의존하지 않기 위한 의도된 선택.
- **RestAssured**: HTTP 레벨 실제 요청. RANDOM_PORT로 전체 서버 기동.
- **DB 상태 검증**: API 응답뿐 아니라 `JdbcTemplate`으로 DB 상태까지 직접 확인한다.

---

> 파일 내용에 대해 추측하지 않는다. 참조하는 파일은 답변 전에 반드시 Read한다. 코드베이스에 대한 주장은 실제 파일을 확인한 후에만 한다.

## 작업 순서

1. 대상 Controller 클래스와 기존 테스트 파일(예: `AccountControllerTest.java`)을
   의존성이 없으므로 병렬로 동시에 Read한다.
2. 필요한 JSON 픽스처 파일을 `acceptance/` 에 추가하거나 기존 것을 재사용한다.
   기존 픽스처 파일: `Glob("app/api/src/test/resources/acceptance/*.json")`
3. 테스트 파일을 작성한다.
4. `./gradlew :app:api:contextTest` 를 실행해 통과를 확인한다.
5. 작성한 파일 목록과 결과를 보고한다.

---

## @AcceptanceTest 구조

```java
@AcceptanceTest({"acceptance/user.json", "acceptance/device.json"})
class AccountControllerTest extends ContextTest {

    @Autowired TestTokenProvider testTokenProvider;
    @Autowired JdbcTemplate jdbcTemplate;

    @Test
    @DisplayName("GUEST 사용자가 탈퇴하면 사용자와 디바이스 정보가 삭제된다.")
    void withdraw_guest() {
        // given / when — Map 사용 (DTO 아님)
        Map<String, Object> response = RestAssured.given()
            .headers(AUTHORIZATION, AUTHENTICATION_TYPE + testTokenProvider.getAccessTokenValue())
            .when().delete("/api/v1/accounts/me")
            .then().extract().jsonPath().getMap(".");

        // then — API 응답 + DB 상태 검증
        assertThat(response).containsEntry("result", "SUCCESS");
        assertThat(jdbcTemplate.queryForObject(
            "SELECT EXISTS (SELECT 1 FROM barlow_user WHERE no = ?)", Integer.class, 1L)).isEqualTo(0);
    }
}
```

---

## TESTING.md 핵심 규칙

`.claude/skills/coding-rules/resources/TESTING.md` 를 읽어 §4(유스케이스 테스트)·§7(테스트 구조 규칙)·§8(테스트 대역)·§10(금지 패턴)을 따른다.

### API 특화 금지 패턴

- DTO 클래스로 요청/응답 직접 매핑 금지 (`extract().as(AccountResponse.class)`)
- `@MockBean` / `@SpyBean` 남용 금지 (컨텍스트 캐시 오염)

---

요청된 변경 범위에 해당하는 테스트만 작성한다.
기존 테스트를 개선하거나 추가 커버리지를 늘리지 않는다.
테스트 파일에 코드 리팩토링이나 주석 정리를 함께 수행하지 않는다.

## 보고 형식

```
[완료] app:api 인수 테스트 작성

작성 파일:
- app/api/src/test/java/com/barlow/app/api/controller/v1/{bc}/{ClassName}Test.java
- app/api/src/test/resources/acceptance/{table}.json  (신규인 경우)

검증 결과: BUILD SUCCESSFUL (N tests)
```

</execution_rules>