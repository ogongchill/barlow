package com.barlow.core.api.controller.v1.home;

import static com.barlow.core.support.TestHttpUtils.AUTHENTICATION_TYPE;
import static com.barlow.core.support.TestHttpUtils.AUTHORIZATION;
import static com.barlow.core.support.TestHttpUtils.MANDATORY_DEVICE_HEADERS;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;

import com.barlow.core.ContextTest;
import com.barlow.core.support.AcceptanceTest;
import com.barlow.core.support.TestTokenProvider;
import com.barlow.core.support.response.ResultType;

import io.restassured.RestAssured;

@AcceptanceTest({
	"acceptance/legislationAccount.json",
	"acceptance/notificationCenter.json",
	"acceptance/legislationAccountSubscribe.json"
})
@Import(TestTokenProvider.class)
class HomeRetrieveControllerTest extends ContextTest {

	@Autowired
	private TestTokenProvider testTokenProvider;

	@DisplayName("메인 홈 조회")
	@Nested
	class RetrieveHomeTest {

		@DisplayName("사용자가 메인 홈을 조회하면 사용자가 구독한 상임위원회 정보와 오늘 도착한 알림 여부를 반환한다")
		@Test
		void retrieveHome() {
			Map<String, Object> responseMap = RestAssured
				.given().log().all().contentType(MediaType.APPLICATION_JSON_VALUE)
				.headers(AUTHORIZATION, AUTHENTICATION_TYPE + testTokenProvider.getAccessTokenValue())
				.headers(MANDATORY_DEVICE_HEADERS)
				.when()
				.get("/api/v1/home")
				.then().log().all().extract()
				.jsonPath().getMap(".");
			assertAll(
				() -> assertThat(responseMap).containsEntry("result", ResultType.SUCCESS.name()),
				() -> assertThat(responseMap.get("data")).isNotNull(),
				() -> assertThat(responseMap.get("error")).isNull()
			);
		}
	}

	@DisplayName("메인 홈 상단 알림센터 조회")
	@Nested
	class RetrieveNotificationCenterTest {

		@DisplayName("사용자가 알림센터를 조회하면 최근에 도착한 알림 정보들을 반환한다")
		@Test
		void retrieveNotificationCenter() {
			Map<String, Object> responseMap = RestAssured
				.given().log().all().contentType(MediaType.APPLICATION_JSON_VALUE)
				.headers(AUTHORIZATION, AUTHENTICATION_TYPE + testTokenProvider.getAccessTokenValue())
				.headers(MANDATORY_DEVICE_HEADERS)
				.when()
				.get("/api/v1/home/notification-center")
				.then().log().all().extract()
				.jsonPath().getMap(".");
			assertAll(
				() -> assertThat(responseMap).containsEntry("result", ResultType.SUCCESS.name()),
				() -> assertThat(responseMap.get("data")).isNotNull(),
				() -> assertThat(responseMap.get("error")).isNull()
			);
		}
	}
}