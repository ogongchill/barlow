package com.barlow.app.api.controller.v1.auth;

import static com.barlow.app.support.TestHttpUtils.MANDATORY_DEVICE_HEADERS;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;

import com.barlow.ContextTest;
import com.barlow.app.support.AcceptanceTest;
import com.barlow.app.support.response.ResultType;
import com.barlow.core.domain.User;
import com.barlow.core.domain.registration.ExternalPrincipal;
import com.barlow.core.enumerate.AuthProvider;
import com.barlow.services.auth.authentication.core.AuthenticationException;
import com.barlow.services.auth.authentication.core.AuthenticationExceptionType;
import com.barlow.services.auth.authentication.oauth.OidcAuthenticationRequest;
import com.barlow.services.auth.authentication.oauth.OidcAuthenticationService;
import com.barlow.services.auth.authentication.token.AccessTokenProvider;

import io.restassured.RestAssured;

@AcceptanceTest({"acceptance/user.json", "acceptance/device.json", "acceptance/term.json"})
class AuthControllerTest extends ContextTest {

	private static final String TEST_SUB = "test_subject_123";

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	AccessTokenProvider accessTokenProvider;

	@MockBean
	OidcAuthenticationService mockOidcService;

	@BeforeEach
	void setUpMock() {
		given(mockOidcService.authenticate(any(OidcAuthenticationRequest.class)))
			.willReturn(new ExternalPrincipal(AuthProvider.KAKAO, TEST_SUB));
	}

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

	@DisplayName("OIDC 회원가입")
	@Nested
	class OidcSignupTest {

		@DisplayName("사용자가 OIDC 회원가입을 하면 회원가입 절차를 진행하고 access token을 반환한다")
		@Test
		void oidcSignup_success() {
			// when
			Map<String, Object> responseMap = RestAssured.given().log().all()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.when()
				.body(Map.of(
					"oidcRequest", Map.of(
						"authProvider", "KAKAO",
						"idToken", "mock_id_token"
					),
					"termAgreementRequest", Map.of(
						"termAgreements", Map.of(
							"1", true,
							"2", true,
							"3", false
						)
					),
					"signupRequest", Map.of(
						"deviceOs", "ios",
						"deviceId", "oidc_device_id",
						"deviceToken", "oidc_device_token",
						"nickname", "oidc_user"
					)
				))
				.post("/api/v1/auth/oidc/signup")
				.then().log().all().extract()
				.jsonPath().getMap(".");

			// then - API 응답 검증
			assertAll(
				() -> assertThat(responseMap).containsEntry("result", ResultType.SUCCESS.name()),
				() -> assertThat(responseMap.get("data")).isNotNull(),
				() -> assertThat(responseMap.get("error")).isNull()
			);

			// then - DB 저장 검증
			Map<String, Object> savedUser = jdbcTemplate.queryForMap(
				"SELECT * FROM barlow_user WHERE nickname = ?", "oidc_user"
			);
			Long memberNo = ((Number) savedUser.get("NO")).longValue(); // h2: uppercase column.

			List<Map<String, Object>> termAgreements = jdbcTemplate.queryForList(
				"SELECT * FROM term_agreement WHERE member_no = ?", memberNo
			);

			Map<String, Object> authProvider = jdbcTemplate.queryForMap(
				"SELECT * FROM auth_provider WHERE member_no = ?", memberNo
			);

			assertAll(
				() -> assertThat(savedUser).containsEntry("ROLE", "MEMBER"),
				() -> assertThat(savedUser).containsEntry("NICKNAME", "oidc_user"),
				() -> assertThat(termAgreements).hasSize(3),
				() -> assertThat(authProvider).containsEntry("PROVIDER", "KAKAO"),
				() -> assertThat(authProvider).containsEntry("SUB", TEST_SUB)
			);
		}

		@DisplayName("필수 약관에 동의하지 않으면 회원가입에 실패한다")
		@Test
		void oidcSignup_failWhenRequiredTermNotAgreed() {
			// given
			String targetNickname = UUID.randomUUID().toString();

			// when
			Map<String, Object> responseMap = RestAssured.given().log().all()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.when()
				.body(Map.of(
					"oidcRequest", Map.of(
						"authProvider", "KAKAO",
						"idToken", "mock_id_token"
					),
					"termAgreementRequest", Map.of(
						"termAgreements", Map.of(
							"1", false,
							"2", true,
							"3", false
						)
					),
					"signupRequest", Map.of(
						"deviceOs", "ios",
						"deviceId", "oidc_device_id_2",
						"deviceToken", "oidc_device_token_2",
						"nickname", targetNickname
					)
				))
				.post("/api/v1/auth/oidc/signup")
				.then().log().all().extract()
				.jsonPath().getMap(".");

			// then
			assertThat(responseMap).containsEntry("result", ResultType.ERROR.name());

			// then - 해당 요청으로 인한 데이터가 생성되지 않았음을 검증
			boolean userCreated = jdbcTemplate.queryForObject(
				"SELECT EXISTS (SELECT 1 FROM barlow_user WHERE nickname = ?)", Integer.class, targetNickname) == 1;
			boolean authProviderCreated = jdbcTemplate.queryForObject(
				"SELECT EXISTS (SELECT 1 FROM auth_provider WHERE sub = ? AND provider = ?)", Integer.class, TEST_SUB, "KAKAO") == 1;

			assertAll(
				() -> assertThat(userCreated).isFalse(),
				() -> assertThat(authProviderCreated).isFalse()
			);
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
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.when()
				.body(Map.of(
					"oidcRequest", Map.of(
						"authProvider", "KAKAO",
						"idToken", "mock_id_token"
					),
					"termAgreementRequest", Map.of(
						"termAgreements", Map.of(
							"1", true,
							"2", true,
							"3", false
						)
					),
					"signupRequest", Map.of(
						"deviceOs", "ios",
						"deviceId", "duplicate_device_id",
						"deviceToken", "duplicate_device_token",
						"nickname", targetNickname
					)
				))
				.post("/api/v1/auth/oidc/signup")
				.then().log().all().extract()
				.jsonPath().getMap(".");

			// then
			assertThat(responseMap).containsEntry("result", ResultType.ERROR.name());

			// then - 해당 요청으로 인한 데이터가 생성되지 않았음을 검증
			boolean userCreated = jdbcTemplate.queryForObject(
				"SELECT EXISTS (SELECT 1 FROM barlow_user WHERE nickname = ?)", Integer.class, targetNickname) == 1;
			int authProviderCountForSub = jdbcTemplate.queryForObject(
				"SELECT COUNT(*) FROM auth_provider WHERE sub = ? AND provider = ?", Integer.class, existingSub, "KAKAO");

			assertAll(
				() -> assertThat(userCreated).isFalse(),
				() -> assertThat(authProviderCountForSub).isEqualTo(1) // 기존 1개만 존재, 추가되지 않음
			);
		}

		@DisplayName("지원하지 않는 authProvider로 회원가입 시 실패한다")
		@Test
		void oidcSignup_failWhenUnsupportedAuthProvider() {
			// given
			String targetNickname = UUID.randomUUID().toString();

			// when
			Map<String, Object> responseMap = RestAssured.given().log().all()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.when()
				.body(Map.of(
					"oidcRequest", Map.of(
						"authProvider", "GOOGLE",
						"idToken", "mock_id_token"
					),
					"termAgreementRequest", Map.of(
						"termAgreements", Map.of(
							"1", true,
							"2", true,
							"3", false
						)
					),
					"signupRequest", Map.of(
						"deviceOs", "ios",
						"deviceId", "unsupported_device_id",
						"deviceToken", "unsupported_device_token",
						"nickname", targetNickname
					)
				))
				.post("/api/v1/auth/oidc/signup")
				.then().log().all().extract()
				.jsonPath().getMap(".");

			// then
			assertThat(responseMap).containsEntry("result", ResultType.ERROR.name());

			// then - 해당 요청으로 인한 데이터가 생성되지 않았음을 검증
			boolean userCreated = jdbcTemplate.queryForObject(
				"SELECT EXISTS (SELECT 1 FROM barlow_user WHERE nickname = ?)", Integer.class, targetNickname) == 1;

			assertThat(userCreated).isFalse();
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
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.when()
				.body(Map.of(
					"oidcRequest", Map.of(
						"authProvider", "KAKAO",
						"idToken", "invalid_token"
					),
					"termAgreementRequest", Map.of(
						"termAgreements", Map.of(
							"1", true,
							"2", true,
							"3", false
						)
					),
					"signupRequest", Map.of(
						"deviceOs", "ios",
						"deviceId", "invalid_token_device_id",
						"deviceToken", "invalid_token_device_token",
						"nickname", targetNickname
					)
				))
				.post("/api/v1/auth/oidc/signup")
				.then().log().all().extract()
				.jsonPath().getMap(".");

			// then
			assertThat(responseMap).containsEntry("result", ResultType.ERROR.name());

			// then - 해당 요청으로 인한 데이터가 생성되지 않았음을 검증
			boolean userCreated = jdbcTemplate.queryForObject(
				"SELECT EXISTS (SELECT 1 FROM barlow_user WHERE nickname = ?)", Integer.class, targetNickname) == 1;

			assertThat(userCreated).isFalse();
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
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.headers("Authorization", "Bearer " + accessToken)
				.headers(MANDATORY_DEVICE_HEADERS)
				.when()
				.body(Map.of(
					"oidcRequest", Map.of(
						"authProvider", "KAKAO",
						"idToken", "mock_id_token"
					),
					"termAgreementRequest", Map.of(
						"termAgreements", Map.of(
							"1", true,
							"2", true,
							"3", false
						)
					)
				))
				.post("/api/v1/auth/oidc/promote")
				.then().log().all().extract()
				.jsonPath().getMap(".");

			// then - API 응답 검증
			assertAll(
				() -> assertThat(responseMap).containsEntry("result", ResultType.SUCCESS.name()),
				() -> assertThat(responseMap.get("data")).isNotNull(),
				() -> assertThat(responseMap.get("error")).isNull()
			);

			// then - DB 저장 검증
			Map<String, Object> updatedUser = jdbcTemplate.queryForMap(
				"SELECT * FROM barlow_user WHERE no = ?", 1L
			);

			List<Map<String, Object>> termAgreements = jdbcTemplate.queryForList(
				"SELECT * FROM term_agreement WHERE member_no = ?", 1L
			);

			Map<String, Object> authProvider = jdbcTemplate.queryForMap(
				"SELECT * FROM auth_provider WHERE member_no = ?", 1L
			);

			assertAll(
				() -> assertThat(updatedUser).containsEntry("ROLE", "MEMBER"),
				() -> assertThat(termAgreements).hasSize(3),
				() -> assertThat(authProvider).containsEntry("PROVIDER", "KAKAO"),
				() -> assertThat(authProvider).containsEntry("SUB", TEST_SUB)
			);
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
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.headers("Authorization", "Bearer " + accessToken)
				.headers(MANDATORY_DEVICE_HEADERS)
				.when()
				.body(Map.of(
					"oidcRequest", Map.of(
						"authProvider", "KAKAO",
						"idToken", "mock_id_token"
					),
					"termAgreementRequest", Map.of(
						"termAgreements", Map.of(
							"1", true,
							"2", true,
							"3", false
						)
					)
				))
				.post("/api/v1/auth/oidc/promote")
				.then().log().all().extract()
				.jsonPath().getMap(".");

			// then
			assertThat(responseMap).containsEntry("result", ResultType.ERROR.name());

			// then - 해당 요청으로 인한 데이터가 생성되지 않았음을 검증
			boolean authProviderCreated = jdbcTemplate.queryForObject(
				"SELECT EXISTS (SELECT 1 FROM auth_provider WHERE member_no = ? AND sub = ?)", Integer.class, targetMemberNo, anotherSub) == 1;

			assertThat(authProviderCreated).isFalse();
		}

		@DisplayName("지원하지 않는 authProvider로 promote 시 실패한다")
		@Test
		void oidcPromote_failWhenUnsupportedAuthProvider() {
			// given
			Long targetMemberNo = 1L;
			String accessToken = getAccessTokenForUser(targetMemberNo, User.Role.GUEST);

			// when
			Map<String, Object> responseMap = RestAssured.given().log().all()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.headers("Authorization", "Bearer " + accessToken)
				.headers(MANDATORY_DEVICE_HEADERS)
				.when()
				.body(Map.of(
					"oidcRequest", Map.of(
						"authProvider", "GOOGLE",
						"idToken", "mock_id_token"
					),
					"termAgreementRequest", Map.of(
						"termAgreements", Map.of(
							"1", true,
							"2", true,
							"3", false
						)
					)
				))
				.post("/api/v1/auth/oidc/promote")
				.then().log().all().extract()
				.jsonPath().getMap(".");

			// then
			assertThat(responseMap).containsEntry("result", ResultType.ERROR.name());

			// then - 사용자 role이 변경되지 않았음을 검증
			String userRole = jdbcTemplate.queryForObject(
				"SELECT role FROM barlow_user WHERE no = ?", String.class, targetMemberNo);
			boolean authProviderCreated = jdbcTemplate.queryForObject(
				"SELECT EXISTS (SELECT 1 FROM auth_provider WHERE member_no = ?)", Integer.class, targetMemberNo) == 1;

			assertAll(
				() -> assertThat(userRole).isEqualTo("GUEST"),
				() -> assertThat(authProviderCreated).isFalse()
			);
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
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.headers("Authorization", "Bearer " + accessToken)
				.headers(MANDATORY_DEVICE_HEADERS)
				.when()
				.body(Map.of(
					"oidcRequest", Map.of(
						"authProvider", "KAKAO",
						"idToken", "invalid_token"
					),
					"termAgreementRequest", Map.of(
						"termAgreements", Map.of(
							"1", true,
							"2", true,
							"3", false
						)
					)
				))
				.post("/api/v1/auth/oidc/promote")
				.then().log().all().extract()
				.jsonPath().getMap(".");

			// then
			assertThat(responseMap).containsEntry("result", ResultType.ERROR.name());

			// then - 사용자 role이 변경되지 않았음을 검증
			String userRole = jdbcTemplate.queryForObject(
				"SELECT role FROM barlow_user WHERE no = ?", String.class, targetMemberNo);
			boolean authProviderCreated = jdbcTemplate.queryForObject(
				"SELECT EXISTS (SELECT 1 FROM auth_provider WHERE member_no = ?)", Integer.class, targetMemberNo) == 1;

			assertAll(
				() -> assertThat(userRole).isEqualTo("GUEST"),
				() -> assertThat(authProviderCreated).isFalse()
			);
		}
	}

	private String getAccessTokenForUser(Long userNo, User.Role role) {
		User user = User.of(userNo, role);
		return accessTokenProvider.issue(user).getValue();
	}
}