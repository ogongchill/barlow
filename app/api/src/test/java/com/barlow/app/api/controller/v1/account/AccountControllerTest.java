package com.barlow.app.api.controller.v1.account;

import static com.barlow.app.support.TestHttpUtils.AUTHENTICATION_TYPE;
import static com.barlow.app.support.TestHttpUtils.AUTHORIZATION;
import static com.barlow.app.support.TestHttpUtils.X_CLIENT_OS;
import static com.barlow.app.support.TestHttpUtils.X_CLIENT_OS_VERSION;
import static com.barlow.app.support.TestHttpUtils.X_DEVICE_ID;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;

import com.barlow.ContextTest;
import com.barlow.app.support.AcceptanceTest;
import com.barlow.app.support.TestTokenProvider;
import com.barlow.app.support.response.ResultType;
import com.barlow.core.domain.User;

import io.restassured.RestAssured;

@AcceptanceTest({
	"acceptance/user.json",
	"acceptance/device.json",
	"acceptance/term.json",
	"acceptance/notificationCenter.json",
	"acceptance/legislationAccount.json",
	"acceptance/legislationAccountNotificationSetting.json",
	"acceptance/legislationAccountSubscribe.json"
})
@Import(TestTokenProvider.class)
class AccountControllerTest extends ContextTest {

	@Autowired
	private TestTokenProvider testTokenProvider;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@DisplayName("회원 탈퇴")
	@Nested
	class GuestWithdrawTest {

		@DisplayName("게스트 사용자가 탈퇴하면 사용자와 디바이스 정보가 삭제된다")
		@Test
		void withdraw_guest() {
			// given
			Long targetUserNo = 1L;
			String targetDeviceId = "device_id_1";

			// when
			Map<String, Object> responseMap = RestAssured
				.given().log().all().contentType(MediaType.APPLICATION_JSON_VALUE)
				.headers(AUTHORIZATION, AUTHENTICATION_TYPE + testTokenProvider.getAccessTokenValue())
				.headers(X_CLIENT_OS, "ios")
				.headers(X_CLIENT_OS_VERSION, "device_os_version")
				.headers(X_DEVICE_ID, targetDeviceId)
				.when()
				.post("/api/v1/account/withdraw")
				.then().log().all().extract()
				.jsonPath().getMap(".");

			// then - API 응답 검증
			assertAll(
				() -> assertThat(responseMap).containsEntry("result", ResultType.SUCCESS.name()),
				() -> assertThat(responseMap.get("data")).isNull(),
				() -> assertThat(responseMap.get("error")).isNull()
			);

			// then - DB 삭제 검증
			boolean userExists = jdbcTemplate.queryForObject(
				"SELECT EXISTS (SELECT 1 FROM barlow_user WHERE no = ?)", Integer.class, targetUserNo) == 1;
			boolean deviceExists = jdbcTemplate.queryForObject(
				"SELECT EXISTS (SELECT 1 FROM device WHERE device_id = ?)", Integer.class, targetDeviceId) == 1;

			assertAll(
				() -> assertThat(userExists).isFalse(),
				() -> assertThat(deviceExists).isFalse()
			);
		}

		@DisplayName("멤버 사용자가 탈퇴하면 사용자, 디바이스, auth_provider, term_agreement 정보가 삭제된다")
		@Test
		void withdraw_member() {
			// given
			Long targetUserNo = 2L;
			String targetDeviceId = "device_id_3";

			// when
			Map<String, Object> responseMap = RestAssured
					.given().log().all().contentType(MediaType.APPLICATION_JSON_VALUE)
					.headers(AUTHORIZATION, AUTHENTICATION_TYPE + testTokenProvider.getAccessTokenValue(targetUserNo, User.Role.MEMBER))
					.headers(X_CLIENT_OS, "android")
					.headers(X_CLIENT_OS_VERSION, "device_os_version")
					.headers(X_DEVICE_ID, targetDeviceId)
					.when()
					.post("/api/v1/account/withdraw")
					.then().log().all().extract()
					.jsonPath().getMap(".");

			// then - API 응답 검증
			assertAll(
					() -> assertThat(responseMap).containsEntry("result", ResultType.SUCCESS.name()),
					() -> assertThat(responseMap.get("data")).isNull(),
					() -> assertThat(responseMap.get("error")).isNull()
			);

			// then - DB 삭제 검증
			boolean userExists = jdbcTemplate.queryForObject(
					"SELECT EXISTS (SELECT 1 FROM barlow_user WHERE no = ?)", Integer.class, targetUserNo) == 1;
			boolean deviceExists = jdbcTemplate.queryForObject(
					"SELECT EXISTS (SELECT 1 FROM device WHERE device_id = ?)", Integer.class, targetDeviceId) == 1;
			boolean authProviderExists = jdbcTemplate.queryForObject(
					"SELECT EXISTS (SELECT 1 FROM auth_provider WHERE member_no = ?)", Integer.class, targetUserNo) == 1;
			boolean termAgreementExists = jdbcTemplate.queryForObject(
					"SELECT EXISTS (SELECT 1 FROM term_agreement WHERE member_no = ?)", Integer.class, targetUserNo) == 1;

			assertAll(
					() -> assertThat(userExists).isFalse(),
					() -> assertThat(deviceExists).isFalse(),
					() -> assertThat(authProviderExists).isFalse(),
					() -> assertThat(termAgreementExists).isFalse()
			);
		}
	}
}