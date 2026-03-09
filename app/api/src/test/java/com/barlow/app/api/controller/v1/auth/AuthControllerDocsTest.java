package com.barlow.app.api.controller.v1.auth;

import static com.barlow.app.support.TestHttpUtils.MANDATORY_DEVICE_HEADERS;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;

import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;

import com.barlow.app.support.AcceptanceTest;
import com.barlow.app.support.FakeOidcAuthenticationService;
import com.barlow.app.support.RestDocsContextTest;
import com.barlow.core.domain.User;
import com.barlow.core.domain.externalauth.ExternalPrincipal;
import com.barlow.core.enumerate.AuthProvider;
import com.barlow.infra.auth.authentication.token.AccessTokenProvider;

import io.restassured.RestAssured;

@AcceptanceTest({"acceptance/user.json", "acceptance/device.json", "acceptance/term.json"})
@Import(FakeOidcAuthenticationService.class)
class AuthControllerDocsTest extends RestDocsContextTest {

	private static final String TEST_SUB = "test_subject_123";

	@Autowired
	private AccessTokenProvider accessTokenProvider;

	@Autowired
	private FakeOidcAuthenticationService fakeOidcService;

	@BeforeEach
	void setUpFake() {
		fakeOidcService.willReturn(new ExternalPrincipal(AuthProvider.KAKAO, TEST_SUB));
	}

	@AfterEach
	void resetFake() {
		fakeOidcService.reset();
	}

	@DisplayName("게스트 계정 생성 API 문서화")
	@Test
	void guestSignup() {
		RestAssured.given(spec)
			.filter(document("auth/guest-signup",
				requestFields(
					fieldWithPath("deviceOs").description("디바이스 OS (ios / android)"),
					fieldWithPath("deviceId").description("디바이스 고유 ID"),
					fieldWithPath("deviceToken").description("푸시 알림용 디바이스 토큰"),
					fieldWithPath("nickname").description("사용자 닉네임"),
					subsectionWithPath("termAgreements").description("약관 동의 목록 (약관 ID → 동의 여부)")),
				relaxedResponseFields(
					fieldWithPath("result").description("결과 상태 (SUCCESS)"),
					fieldWithPath("data.accessToken").description("발급된 액세스 토큰"))))
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(Map.of(
				"deviceOs", "ios",
				"deviceId", "device_id_new",
				"deviceToken", "device_token_new",
				"nickname", "nniicckknnaammee",
				"termAgreements", Map.of("1", true, "2", true, "3", false)))
			.when()
			.post("/api/v1/auth/guests")
			.then()
			.statusCode(201);
	}

	@DisplayName("게스트 세션 생성 (로그인) API 문서화")
	@Test
	void guestLogin() {
		RestAssured.given(spec)
			.filter(document("auth/guest-login",
				requestFields(
					fieldWithPath("deviceOs").description("디바이스 OS (ios / android)"),
					fieldWithPath("deviceId").description("디바이스 고유 ID"),
					fieldWithPath("deviceToken").description("푸시 알림용 디바이스 토큰")),
				relaxedResponseFields(
					fieldWithPath("result").description("결과 상태 (SUCCESS)"),
					fieldWithPath("data.accessToken").description("발급된 액세스 토큰"))))
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(Map.of("deviceOs", "ios", "deviceId", "device_id_1", "deviceToken", "device_token_1"))
			.when()
			.post("/api/v1/auth/guest/sessions")
			.then()
			.statusCode(200);
	}

	@DisplayName("OIDC 멤버 계정 생성 API 문서화")
	@Test
	void oidcSignup() {
		RestAssured.given(spec)
			.filter(document("auth/oidc-signup",
				requestFields(
					subsectionWithPath("oidcPayload").description("OIDC 인증 정보 (authProvider, idToken)"),
					subsectionWithPath("termAgreements").description("약관 동의 목록 (약관 ID → 동의 여부)"),
					subsectionWithPath("signupPayload")
						.description("회원가입 기기 정보 (deviceOs, deviceId, deviceToken, nickname)")),
				relaxedResponseFields(
					fieldWithPath("result").description("결과 상태 (SUCCESS)"),
					fieldWithPath("data.accessToken").description("발급된 액세스 토큰"))))
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(Map.of(
				"oidcPayload", Map.of("authProvider", "KAKAO", "idToken", "mock_id_token"),
				"termAgreements", Map.of("1", true, "2", true, "3", false),
				"signupPayload", Map.of(
					"deviceOs", "ios",
					"deviceId", "oidc_device_id",
					"deviceToken", "oidc_device_token",
					"nickname", "oidc_user")))
			.when()
			.post("/api/v1/auth/oidc/accounts")
			.then()
			.statusCode(201);
	}

	@DisplayName("OIDC 역할 전환 (게스트 → 멤버) API 문서화")
	@Test
	void oidcPromote() {
		String accessToken = accessTokenProvider.issue(User.of(1L, User.Role.GUEST)).getValue();

		RestAssured.given(spec)
			.filter(document("auth/oidc-promote",
				requestHeaders(
					headerWithName("Authorization").description("Bearer 액세스 토큰 (GUEST 역할)")),
				requestFields(
					subsectionWithPath("oidcPayload").description("OIDC 인증 정보 (authProvider, idToken)")),
				relaxedResponseFields(
					fieldWithPath("result").description("결과 상태 (SUCCESS)"),
					fieldWithPath("data.accessToken").description("MEMBER 역할로 재발급된 액세스 토큰"))))
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.header("Authorization", "Bearer " + accessToken)
			.headers(MANDATORY_DEVICE_HEADERS)
			.body(Map.of("oidcPayload", Map.of("authProvider", "KAKAO", "idToken", "mock_id_token")))
			.when()
			.patch("/api/v1/auth/oidc/role")
			.then()
			.statusCode(200);
	}

	@DisplayName("OIDC 세션 생성 (로그인) API 문서화")
	@Test
	void oidcLogin() {
		fakeOidcService.willReturn(new ExternalPrincipal(AuthProvider.KAKAO, "existing_sub_123"));

		RestAssured.given(spec)
			.filter(document("auth/oidc-login",
				requestFields(
					fieldWithPath("deviceOs").description("디바이스 OS (ios / android)"),
					fieldWithPath("deviceId").description("디바이스 고유 ID"),
					fieldWithPath("deviceToken").description("푸시 알림용 디바이스 토큰"),
					subsectionWithPath("oidcPayload").description("OIDC 인증 정보 (authProvider, idToken)")),
				relaxedResponseFields(
					fieldWithPath("result").description("결과 상태 (SUCCESS)"),
					fieldWithPath("data.accessToken").description("발급된 액세스 토큰"))))
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(Map.of(
				"deviceOs", "android",
				"deviceId", "device_id_3",
				"deviceToken", "device_token_3",
				"oidcPayload", Map.of("authProvider", "KAKAO", "idToken", "mock_id_token")))
			.when()
			.post("/api/v1/auth/oidc/sessions")
			.then()
			.statusCode(200);
	}
}
