package com.barlow.app.api.controller.v1.auth;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import com.barlow.core.ContextTest;
import com.barlow.core.support.AcceptanceTest;
import com.barlow.core.support.response.ResultType;

import io.restassured.RestAssured;

@AcceptanceTest({"acceptance/user.json", "acceptance/device.json"})
class AuthControllerTest extends ContextTest {

	@DisplayName("사용자가 게스트 회원가입을 하면 회원가입 절차를 진행하고 access token 을 반환한다")
	@Test
	void guestSignup() {
		Map<String, Object> responseMap = RestAssured.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when()
			.body(Map.of(
				"deviceOs", "ios",
				"deviceId", "device_id_new",
				"deviceToken", "device_token_new",
				"nickname", "nickname"
			))
			.post("/api/v1/auth/guest/signup")
			.then().log().all().extract()
			.jsonPath().getMap(".");
		assertAll(
			() -> assertThat(responseMap).containsEntry("result", ResultType.SUCCESS.name()),
			() -> assertThat(responseMap.get("data")).isNotNull(),
			() -> assertThat(responseMap.get("error")).isNull()
		);
	}

	@DisplayName("게스트 로그인")
	@Nested
	class GuestLoginTest {

		@DisplayName("사용자가 게스트 로그인을 하면 로그인 절차를 진행하고 access token 을 반환한다")
		@Test
		void guestLogin_tokenNotChanged() {
			Map<String, Object> responseMap = RestAssured.given().log().all()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.when()
				.body(Map.of(
					"deviceOs", "ios",
					"deviceId", "device_id_1",
					"deviceToken", "device_token_1"
				))
				.post("/api/v1/auth/guest/login")
				.then().log().all().extract()
				.jsonPath().getMap(".");
			assertAll(
				() -> assertThat(responseMap).containsEntry("result", ResultType.SUCCESS.name()),
				() -> assertThat(responseMap.get("data")).isNotNull(),
				() -> assertThat(responseMap.get("error")).isNull()
			);
		}

		@DisplayName("사용자가 게스트 로그인 시 디바이스 토큰이 바뀌었다면 로그인 절차 중 디바이스 토큰을 변경한 후 access token 을 반환한다")
		@Test
		void guestLogin_tokenChanged() {
			Map<String, Object> responseMap = RestAssured.given().log().all()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.when()
				.body(Map.of(
					"deviceOs", "ios",
					"deviceId", "device_id_1",
					"deviceToken", "changed_device_token"
				))
				.post("/api/v1/auth/guest/login")
				.then().log().all().extract()
				.jsonPath().getMap(".");
			assertAll(
				() -> assertThat(responseMap).containsEntry("result", ResultType.SUCCESS.name()),
				() -> assertThat(responseMap.get("data")).isNotNull(),
				() -> assertThat(responseMap.get("error")).isNull()
			);
		}

		@DisplayName("사용자가 게스트 로그인 시 디바이스가 비활성화 상태라면 예외를 발생시킨다")
		@Test
		void guestLogin_deviceInactive() {
			Map<String, Object> responseMap = RestAssured.given().log().all()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.when()
				.body(Map.of(
					"deviceOs", "ANDROID",
					"deviceId", "device_id_2",
					"deviceToken", "device_token_2"
				))
				.post("/api/v1/auth/guest/login")
				.then().log().all().extract()
				.jsonPath().getMap(".");
			assertThat(responseMap).containsEntry("result", ResultType.ERROR.name());
		}
	}
}