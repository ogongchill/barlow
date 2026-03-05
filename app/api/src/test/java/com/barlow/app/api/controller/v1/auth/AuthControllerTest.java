package com.barlow.app.api.controller.v1.auth;

import static com.barlow.app.support.TestHttpUtils.MANDATORY_DEVICE_HEADERS;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import com.barlow.ContextTest;
import com.barlow.app.support.AcceptanceTest;
import com.barlow.app.support.response.ResultType;
import com.barlow.core.domain.User;
import com.barlow.core.domain.externalauth.ExternalPrincipal;
import com.barlow.core.enumerate.AuthProvider;
import com.barlow.infra.auth.authentication.core.AuthenticationException;
import com.barlow.infra.auth.authentication.core.AuthenticationExceptionType;
import com.barlow.infra.auth.authentication.oauth.OidcAuthenticationRequest;
import com.barlow.infra.auth.authentication.oauth.OidcAuthenticationService;
import com.barlow.infra.auth.authentication.token.AccessTokenProvider;

import io.restassured.RestAssured;

@AcceptanceTest({"acceptance/user.json", "acceptance/device.json", "acceptance/term.json"})
class AuthControllerTest extends ContextTest {

	private static final String TEST_SUB = "test_subject_123";

	@Autowired
	private AccessTokenProvider accessTokenProvider;

	@MockBean
	private OidcAuthenticationService mockOidcService;

	@BeforeEach
	void setUpMock() {
		given(mockOidcService.authenticate(any(OidcAuthenticationRequest.class)))
			.willReturn(new ExternalPrincipal(AuthProvider.KAKAO, TEST_SUB));
	}

	@DisplayName("사용자가 게스트 회원가입을 하면 회원가입 절차를 진행하고 access token 을 반환한다")
	@Test
	void guestSignup() {
		Map<String, Object> responseMap = RestAssured.given().log().all().contentType(MediaType.APPLICATION_JSON_VALUE)
			.when()
			.body(
				Map.of(
					"deviceOs", "ios", "deviceId", "device_id_new", "deviceToken", "device_token_new", "nickname",
					"nniicckknnaammee", "termAgreements", Map.of("1", true, "2", true, "3", false)))
			.post("/api/v1/auth/guest/signup").then().log().all().extract().jsonPath().getMap(".");

		// then - API 응답 검증
		assertAll(
			() -> assertThat(responseMap).containsEntry("result", ResultType.SUCCESS.name()),
			() -> assertThat(responseMap.get("data")).isNotNull(), () -> assertThat(responseMap.get("error")).isNull());
	}

	@DisplayName("게스트 로그인")
	@Nested
	class GuestLoginTest {

		@DisplayName("사용자가 게스트 로그인을 하면 로그인 절차를 진행하고 access token 을 반환한다")
		@Test
		void guestLogin_tokenNotChanged() {
			Map<String, Object> responseMap = RestAssured.given().log().all()
				.contentType(MediaType.APPLICATION_JSON_VALUE).when()
				.body(Map.of("deviceOs", "ios", "deviceId", "device_id_1", "deviceToken", "device_token_1"))
				.post("/api/v1/auth/guest/login").then().log().all().extract().jsonPath().getMap(".");
			assertAll(
				() -> assertThat(responseMap).containsEntry("result", ResultType.SUCCESS.name()),
				() -> assertThat(responseMap.get("data")).isNotNull(),
				() -> assertThat(responseMap.get("error")).isNull());
		}

		@DisplayName("사용자가 게스트 로그인 시 디바이스 토큰이 바뀌었다면 로그인 절차 중 디바이스 토큰을 변경한 후 access token 을 반환한다")
		@Test
		void guestLogin_tokenChanged() {
			Map<String, Object> responseMap = RestAssured.given().log().all()
				.contentType(MediaType.APPLICATION_JSON_VALUE).when()
				.body(Map.of("deviceOs", "ios", "deviceId", "device_id_1", "deviceToken", "changed_device_token"))
				.post("/api/v1/auth/guest/login").then().log().all().extract().jsonPath().getMap(".");
			assertAll(
				() -> assertThat(responseMap).containsEntry("result", ResultType.SUCCESS.name()),
				() -> assertThat(responseMap.get("data")).isNotNull(),
				() -> assertThat(responseMap.get("error")).isNull());
		}

		@DisplayName("사용자가 게스트 로그인 시 디바이스가 비활성화 상태라면 예외를 발생시킨다")
		@Test
		void guestLogin_deviceInactive() {
			Map<String, Object> responseMap = RestAssured.given().log().all()
				.contentType(MediaType.APPLICATION_JSON_VALUE).when()
				.body(Map.of("deviceOs", "ANDROID", "deviceId", "device_id_2", "deviceToken", "device_token_2"))
				.post("/api/v1/auth/guest/login").then().log().all().extract().jsonPath().getMap(".");
			assertThat(responseMap).containsEntry("result", ResultType.ERROR.name());
		}
	}

	@DisplayName("OIDC 회원가입")
	@Nested
	class OidcSignupTest {

		@DisplayName("사용자가 OIDC 회원가입을 하면 회원가입 절차를 진행하고 access token을 반환한다")
		@Test
		void oidcSignup_success() {
			// when
			Map<String, Object> responseMap = RestAssured.given().log().all()
				.contentType(MediaType.APPLICATION_JSON_VALUE).when()
				.body(
					Map.of(
						"oidcPayload", Map.of("authProvider", "KAKAO", "idToken", "mock_id_token"), "termAgreements",
						Map.of("1", true, "2", true, "3", false), "signupPayload",
						Map.of(
							"deviceOs", "ios", "deviceId", "oidc_device_id", "deviceToken", "oidc_device_token",
							"nickname", "oidc_user")))
				.post("/api/v1/auth/oidc/signup").then().log().all().extract().jsonPath().getMap(".");

			// then - API 응답 검증
			assertAll(
				() -> assertThat(responseMap).containsEntry("result", ResultType.SUCCESS.name()),
				() -> assertThat(responseMap.get("data")).isNotNull(),
				() -> assertThat(responseMap.get("error")).isNull());
		}

		@DisplayName("필수 약관에 동의하지 않으면 회원가입에 실패한다")
		@Test
		void oidcSignup_failWhenRequiredTermNotAgreed() {
			// given
			String targetNickname = UUID.randomUUID().toString();

			// when
			Map<String, Object> responseMap = RestAssured.given().log().all()
				.contentType(MediaType.APPLICATION_JSON_VALUE).when()
				.body(
					Map.of(
						"oidcPayload", Map.of("authProvider", "KAKAO", "idToken", "mock_id_token"), "termAgreements",
						Map.of("1", false, "2", true, "3", false), "signupPayload",
						Map.of(
							"deviceOs", "ios", "deviceId", "oidc_device_id_2", "deviceToken", "oidc_device_token_2",
							"nickname", targetNickname)))
				.post("/api/v1/auth/oidc/signup").then().log().all().extract().jsonPath().getMap(".");

			// then
			assertThat(responseMap).containsEntry("result", ResultType.ERROR.name());
		}

		@DisplayName("이미 등록된 provider+sub 조합으로 회원가입 시 실패한다")
		@Test
		void oidcSignup_failWhenProviderSubAlreadyRegistered() {
			// given - existing_sub_123은 user.json에서 이미 등록됨
			String existingSub = "existing_sub_123";
			String targetNickname = UUID.randomUUID().toString();

			given(mockOidcService.authenticate(any(OidcAuthenticationRequest.class)))
				.willReturn(new ExternalPrincipal(AuthProvider.KAKAO, existingSub));

			// when
			Map<String, Object> responseMap = RestAssured.given().log().all()
				.contentType(MediaType.APPLICATION_JSON_VALUE).when()
				.body(
					Map.of(
						"oidcPayload", Map.of("authProvider", "KAKAO", "idToken", "mock_id_token"), "termAgreements",
						Map.of("1", true, "2", true, "3", false), "signupPayload",
						Map.of(
							"deviceOs", "ios", "deviceId", "duplicate_device_id", "deviceToken",
							"duplicate_device_token", "nickname", targetNickname)))
				.post("/api/v1/auth/oidc/signup").then().log().all().extract().jsonPath().getMap(".");

			// then
			assertThat(responseMap).containsEntry("result", ResultType.ERROR.name());
		}

		@DisplayName("지원하지 않는 authProvider로 회원가입 시 실패한다")
		@Test
		void oidcSignup_failWhenUnsupportedAuthProvider() {
			// given
			String targetNickname = UUID.randomUUID().toString();

			// when
			Map<String, Object> responseMap = RestAssured.given().log().all()
				.contentType(MediaType.APPLICATION_JSON_VALUE).when()
				.body(
					Map.of(
						"oidcPayload", Map.of("authProvider", "GOOGLE", "idToken", "mock_id_token"), "termAgreements",
						Map.of("1", true, "2", true, "3", false), "signupPayload",
						Map.of(
							"deviceOs", "ios", "deviceId", "unsupported_device_id", "deviceToken",
							"unsupported_device_token", "nickname", targetNickname)))
				.post("/api/v1/auth/oidc/signup").then().log().all().extract().jsonPath().getMap(".");

			// then
			assertThat(responseMap).containsEntry("result", ResultType.ERROR.name());
		}

		@DisplayName("OIDC 토큰 검증 실패 시 회원가입에 실패한다")
		@Test
		void oidcSignup_failWhenTokenVerificationFails() {
			// given
			String targetNickname = UUID.randomUUID().toString();

			given(mockOidcService.authenticate(any(OidcAuthenticationRequest.class)))
				.willThrow(new AuthenticationException("토큰 검증 실패", AuthenticationExceptionType.INVALID_CREDENTIAL));

			// when
			Map<String, Object> responseMap = RestAssured.given().log().all()
				.contentType(MediaType.APPLICATION_JSON_VALUE).when()
				.body(
					Map.of(
						"oidcPayload", Map.of("authProvider", "KAKAO", "idToken", "invalid_token"), "termAgreements",
						Map.of("1", true, "2", true, "3", false), "signupPayload",
						Map.of(
							"deviceOs", "ios", "deviceId", "invalid_token_device_id", "deviceToken",
							"invalid_token_device_token", "nickname", targetNickname)))
				.post("/api/v1/auth/oidc/signup").then().log().all().extract().jsonPath().getMap(".");

			// then
			assertThat(responseMap).containsEntry("result", ResultType.ERROR.name());
		}
	}

	@DisplayName("OIDC Promote (게스트 → 멤버 전환)")
	@Nested
	class OidcPromoteTest {

		@DisplayName("게스트 사용자가 OIDC를 통해 멤버로 전환되면 access token을 반환한다")
		@Test
		void oidcPromote_success() {
			// given - no=1은 GUEST이고 auth_provider가 없음
			String accessToken = getAccessTokenForUser(1L, User.Role.GUEST);

			// when
			Map<String, Object> responseMap = RestAssured.given().log().all()
				.contentType(MediaType.APPLICATION_JSON_VALUE).headers("Authorization", "Bearer " + accessToken)
				.headers(MANDATORY_DEVICE_HEADERS).when()
				.body(Map.of("oidcPayload", Map.of("authProvider", "KAKAO", "idToken", "mock_id_token")))
				.post("/api/v1/auth/oidc/promote").then().log().all().extract().jsonPath().getMap(".");

			// then - API 응답 검증
			assertAll(
				() -> assertThat(responseMap).containsEntry("result", ResultType.SUCCESS.name()),
				() -> assertThat(responseMap.get("data")).isNotNull(),
				() -> assertThat(responseMap.get("error")).isNull());
		}

		@DisplayName("해당 계정에 이미 Member로 존재할 경우 경우 실패한다")
		@Test
		void oidcPromote_failWhenAlreadyMember() {
			// given - member_no=2는 이미 MEMBER이고 KAKAO provider가 등록됨
			String anotherSub = "another_sub";
			Long targetMemberNo = 2L;
			String accessToken = getAccessTokenForUser(targetMemberNo, User.Role.MEMBER);
			given(mockOidcService.authenticate(any(OidcAuthenticationRequest.class)))
				.willReturn(new ExternalPrincipal(AuthProvider.KAKAO, anotherSub));

			// when
			Map<String, Object> responseMap = RestAssured.given().log().all()
				.contentType(MediaType.APPLICATION_JSON_VALUE).headers("Authorization", "Bearer " + accessToken)
				.headers(MANDATORY_DEVICE_HEADERS).when()
				.body(Map.of("oidcPayload", Map.of("authProvider", "KAKAO", "idToken", "mock_id_token")))
				.post("/api/v1/auth/oidc/promote").then().log().all().extract().jsonPath().getMap(".");

			// then
			assertThat(responseMap).containsEntry("result", ResultType.ERROR.name());
		}

		@DisplayName("지원하지 않는 authProvider로 promote 시 실패한다")
		@Test
		void oidcPromote_failWhenUnsupportedAuthProvider() {
			// given
			Long targetMemberNo = 1L;
			String accessToken = getAccessTokenForUser(targetMemberNo, User.Role.GUEST);

			// when
			Map<String, Object> responseMap = RestAssured.given().log().all()
				.contentType(MediaType.APPLICATION_JSON_VALUE).headers("Authorization", "Bearer " + accessToken)
				.headers(MANDATORY_DEVICE_HEADERS).when()
				.body(Map.of("oidcPayload", Map.of("authProvider", "GOOGLE", "idToken", "mock_id_token")))
				.post("/api/v1/auth/oidc/promote").then().log().all().extract().jsonPath().getMap(".");

			// then
			assertThat(responseMap).containsEntry("result", ResultType.ERROR.name());
		}

		@DisplayName("OIDC 토큰 검증 실패 시 promote에 실패한다")
		@Test
		void oidcPromote_failWhenTokenVerificationFails() {
			// given
			Long targetMemberNo = 1L;
			String accessToken = getAccessTokenForUser(targetMemberNo, User.Role.GUEST);

			given(mockOidcService.authenticate(any(OidcAuthenticationRequest.class)))
				.willThrow(new AuthenticationException("토큰 검증 실패", AuthenticationExceptionType.INVALID_CREDENTIAL));

			// when
			Map<String, Object> responseMap = RestAssured.given().log().all()
				.contentType(MediaType.APPLICATION_JSON_VALUE).headers("Authorization", "Bearer " + accessToken)
				.headers(MANDATORY_DEVICE_HEADERS).when()
				.body(Map.of("oidcPayload", Map.of("authProvider", "KAKAO", "idToken", "invalid_token")))
				.post("/api/v1/auth/oidc/promote").then().log().all().extract().jsonPath().getMap(".");

			// then
			assertThat(responseMap).containsEntry("result", ResultType.ERROR.name());
		}
	}

	@DisplayName("OIDC 로그인")
	@Nested
	class OidcLoginTest {

		@DisplayName("등록된 Member 사용자가 OIDC로 로그인하면 access token을 반환한다")
		@Test
		void oidcLogin_success() {
			// given - member_no=2는 이미 KAKAO provider로 등록됨 (existing_sub_123)
			String existingSub = "existing_sub_123";
			given(mockOidcService.authenticate(any(OidcAuthenticationRequest.class)))
				.willReturn(new ExternalPrincipal(AuthProvider.KAKAO, existingSub));

			// when
			Map<String, Object> responseMap = RestAssured.given().log().all()
				.contentType(MediaType.APPLICATION_JSON_VALUE).when()
				.body(
					Map.of(
						"deviceOs", "android", "deviceId", "device_id_3", "deviceToken", "device_token_3",
						"oidcPayload", Map.of("authProvider", "KAKAO", "idToken", "mock_id_token")))
				.post("/api/v1/auth/oidc/login").then().log().all().extract().jsonPath().getMap(".");

			// then
			assertAll(
				() -> assertThat(responseMap).containsEntry("result", ResultType.SUCCESS.name()),
				() -> assertThat(responseMap.get("data")).isNotNull(),
				() -> assertThat(responseMap.get("error")).isNull());
		}

		@DisplayName("등록되지 않은 provider+sub로 로그인 시 실패한다")
		@Test
		void oidcLogin_failWhenNotRegistered() {
			// given - 등록되지 않은 sub
			String unregisteredSub = "unregistered_sub";
			given(mockOidcService.authenticate(any(OidcAuthenticationRequest.class)))
				.willReturn(new ExternalPrincipal(AuthProvider.KAKAO, unregisteredSub));

			// when
			Map<String, Object> responseMap = RestAssured.given().log().all()
				.contentType(MediaType.APPLICATION_JSON_VALUE).when()
				.body(
					Map.of(
						"deviceOs", "ios", "deviceId", "new_device_id", "deviceToken", "new_device_token",
						"oidcPayload", Map.of("authProvider", "KAKAO", "idToken", "mock_id_token")))
				.post("/api/v1/auth/oidc/login").then().log().all().extract().jsonPath().getMap(".");

			// then
			assertThat(responseMap).containsEntry("result", ResultType.ERROR.name());
		}

		@DisplayName("OIDC 토큰 검증 실패 시 로그인에 실패한다")
		@Test
		void oidcLogin_failWhenTokenVerificationFails() {
			// given
			given(mockOidcService.authenticate(any(OidcAuthenticationRequest.class)))
				.willThrow(new AuthenticationException("토큰 검증 실패", AuthenticationExceptionType.INVALID_CREDENTIAL));

			// when
			Map<String, Object> responseMap = RestAssured.given().log().all()
				.contentType(MediaType.APPLICATION_JSON_VALUE).when()
				.body(
					Map.of(
						"deviceOs", "ios", "deviceId", "device_id", "deviceToken", "device_token", "oidcPayload",
						Map.of("authProvider", "KAKAO", "idToken", "invalid_token")))
				.post("/api/v1/auth/oidc/login").then().log().all().extract().jsonPath().getMap(".");

			// then
			assertThat(responseMap).containsEntry("result", ResultType.ERROR.name());
		}

		@DisplayName("지원하지 않는 authProvider로 로그인 시 실패한다")
		@Test
		void oidcLogin_failWhenUnsupportedAuthProvider() {
			// when
			Map<String, Object> responseMap = RestAssured.given().log().all()
				.contentType(MediaType.APPLICATION_JSON_VALUE).when()
				.body(
					Map.of(
						"deviceOs", "ios", "deviceId", "device_id", "deviceToken", "device_token", "oidcPayload",
						Map.of("authProvider", "GOOGLE", "idToken", "mock_id_token")))
				.post("/api/v1/auth/oidc/login").then().log().all().extract().jsonPath().getMap(".");

			// then
			assertThat(responseMap).containsEntry("result", ResultType.ERROR.name());
		}

		@DisplayName("OIDC 로그인 시 디바이스 토큰이 바뀌었다면 로그인 절차 중 디바이스 토큰을 변경한 후 access token을 반환한다")
		@Test
		void oidcLogin_tokenChanged() {
			// given - member_no=2는 이미 KAKAO provider로 등록됨 (existing_sub_123)
			String existingSub = "existing_sub_123";
			String changedToken = "changed_device_token_for_member";
			given(mockOidcService.authenticate(any(OidcAuthenticationRequest.class)))
				.willReturn(new ExternalPrincipal(AuthProvider.KAKAO, existingSub));

			// when
			Map<String, Object> responseMap = RestAssured.given().log().all()
				.contentType(MediaType.APPLICATION_JSON_VALUE).when()
				.body(
					Map.of(
						"deviceOs", "android", "deviceId", "device_id_3", "deviceToken", changedToken, "oidcPayload",
						Map.of("authProvider", "KAKAO", "idToken", "mock_id_token")))
				.post("/api/v1/auth/oidc/login").then().log().all().extract().jsonPath().getMap(".");

			// then - API 응답 검증
			assertAll(
				() -> assertThat(responseMap).containsEntry("result", ResultType.SUCCESS.name()),
				() -> assertThat(responseMap.get("data")).isNotNull(),
				() -> assertThat(responseMap.get("error")).isNull());
		}

		@DisplayName("OIDC 로그인 시 디바이스가 비활성화 상태라면 예외를 발생시킨다")
		@Test
		void oidcLogin_deviceInactive() {
			// given - member_no=2는 이미 KAKAO provider로 등록됨 (existing_sub_123)
			// device_id_5는 member_no=2의 INACTIVE 디바이스
			String existingSub = "existing_sub_123";
			given(mockOidcService.authenticate(any(OidcAuthenticationRequest.class)))
				.willReturn(new ExternalPrincipal(AuthProvider.KAKAO, existingSub));

			// when
			Map<String, Object> responseMap = RestAssured.given().log().all()
				.contentType(MediaType.APPLICATION_JSON_VALUE).when()
				.body(
					Map.of(
						"deviceOs", "ios", "deviceId", "device_id_5", "deviceToken", "device_token_5", "oidcPayload",
						Map.of("authProvider", "KAKAO", "idToken", "mock_id_token")))
				.post("/api/v1/auth/oidc/login").then().log().all().extract().jsonPath().getMap(".");

			// then
			assertThat(responseMap).containsEntry("result", ResultType.ERROR.name());
		}
	}

	private String getAccessTokenForUser(Long userNo, User.Role role) {
		User user = User.of(userNo, role);
		return accessTokenProvider.issue(user).getValue();
	}
}
