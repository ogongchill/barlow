package com.barlow.app.api.controller.v1.legislationaccount;

import static com.barlow.app.support.TestHttpUtils.AUTHENTICATION_TYPE;
import static com.barlow.app.support.TestHttpUtils.AUTHORIZATION;
import static com.barlow.app.support.TestHttpUtils.MANDATORY_DEVICE_HEADERS;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;

import com.barlow.ContextTest;
import com.barlow.app.support.AcceptanceTest;
import com.barlow.app.support.TestTokenProvider;
import com.barlow.app.support.response.ResultType;
import com.barlow.core.enumerate.LegislationType;

import io.restassured.RestAssured;

@AcceptanceTest("acceptance/legislationAccountNotificationSetting.json")
@Import(TestTokenProvider.class)
class LegislationAccountNotificationSettingControllerTest extends ContextTest {

	@Autowired
	private TestTokenProvider testTokenProvider;

	@DisplayName("상임위원회 별 알림설정 활성화")
	@Nested
	class ActivateNotificationSetting {

		@DisplayName("사용자가 상임위원회의 알림설정을 활성화하면 알림설정을 활성화시키고 성공한다")
		@Test
		void activate_success() {
			Map<String, Object> responseMap = activateNotificationSetting(LegislationType.HOUSE_STEERING);
			assertAll(
				() -> assertThat(responseMap).containsEntry("result", ResultType.SUCCESS.name()),
				() -> assertThat(responseMap.get("data")).isNull(),
				() -> assertThat(responseMap.get("error")).isNull()
			);
		}

		@DisplayName("사용자가 이미 활성화된 상임위원회의 알림설정을 활성화하면 예외를 발생시키고 실패한다")
		@Test
		void activate_fail() {
			Map<String, Object> responseMap = activateNotificationSetting(LegislationType.LEGISLATION_AND_JUDICIARY);
			assertThat(responseMap).containsEntry("result", ResultType.ERROR.name());
		}

		private Map<String, Object> activateNotificationSetting(LegislationType legislationType) {
			return RestAssured
				.given().log().all().contentType(MediaType.APPLICATION_JSON_VALUE)
				.headers(AUTHORIZATION, AUTHENTICATION_TYPE + testTokenProvider.getAccessTokenValue())
				.headers(MANDATORY_DEVICE_HEADERS)
				.when()
				.pathParam("legislationType", legislationType)
				.post("/api/v1/legislation-accounts/{legislationType}/notification-setting/activate")
				.then().log().all().extract()
				.jsonPath().getMap(".");
		}
	}


	@DisplayName("상임위원회 별 알림설정 비활성화")
	@Nested
	class DeactivateNotificationSetting {

		@DisplayName("사용자가 상임위원회의 알림설정을 비활성화하면 알림설정을 비활성화시키고 성공한다")
		@Test
		void deactivate_success() {
			Map<String, Object> responseMap = deactivateNotificationSetting(LegislationType.LEGISLATION_AND_JUDICIARY);
			assertAll(
				() -> assertThat(responseMap).containsEntry("result", ResultType.SUCCESS.name()),
				() -> assertThat(responseMap.get("data")).isNull(),
				() -> assertThat(responseMap.get("error")).isNull()
			);
		}

		@DisplayName("사용자가 이미 비활성화된 상임위원회의 알림설정을 비활성화하면 예외를 발생시키고 실패한다")
		@Test
		void deactivate_fail() {
			Map<String, Object> responseMap = deactivateNotificationSetting(LegislationType.HOUSE_STEERING);
			assertThat(responseMap).containsEntry("result", ResultType.ERROR.name());
		}

		private Map<String, Object> deactivateNotificationSetting(LegislationType legislationType) {
			return RestAssured
				.given().log().all().contentType(MediaType.APPLICATION_JSON_VALUE)
				.headers(AUTHORIZATION, AUTHENTICATION_TYPE + testTokenProvider.getAccessTokenValue())
				.headers(MANDATORY_DEVICE_HEADERS)
				.when()
				.pathParam("legislationType", legislationType)
				.post("/api/v1/legislation-accounts/{legislationType}/notification-setting/deactivate")
				.then().log().all().extract()
				.jsonPath().getMap(".");
		}
	}
}