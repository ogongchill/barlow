package com.barlow.app.api.controller.v1.account;

import static com.barlow.app.support.TestHttpUtils.AUTHENTICATION_TYPE;
import static com.barlow.app.support.TestHttpUtils.AUTHORIZATION;
import static com.barlow.app.support.TestHttpUtils.X_CLIENT_OS;
import static com.barlow.app.support.TestHttpUtils.X_CLIENT_OS_VERSION;
import static com.barlow.app.support.TestHttpUtils.X_DEVICE_ID;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import com.barlow.app.support.AcceptanceTest;
import com.barlow.app.support.RestDocsContextTest;
import com.barlow.core.domain.User;

import io.restassured.RestAssured;

@AcceptanceTest({"acceptance/user.json", "acceptance/device.json", "acceptance/term.json",
	"acceptance/notificationCenter.json", "acceptance/legislationAccount.json",
	"acceptance/legislationAccountNotificationSetting.json", "acceptance/legislationAccountSubscribe.json"})
class AccountControllerDocsTest extends RestDocsContextTest {

	@DisplayName("내 계정 조회 API 문서화")
	@Test
	void getMyAccount() {
		RestAssured.given(spec)
			.filter(document("account/get-my",
				authRequestHeaders(),
				relaxedResponseFields(
					fieldWithPath("result").description("결과 상태 (SUCCESS)"),
					subsectionWithPath("data.user").description("사용자 정보"),
					fieldWithPath("data.user.userNo").description("사용자 번호"),
					fieldWithPath("data.user.nickname").description("닉네임"),
					fieldWithPath("data.user.role").description("사용자 역할 (GUEST / MEMBER)"),
					subsectionWithPath("data.authProviders").description("연결된 소셜 로그인 제공자 목록"),
					subsectionWithPath("data.devices").description("연결된 기기 목록"))))
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.header(AUTHORIZATION, AUTHENTICATION_TYPE + testTokenProvider.getAccessTokenValue(2L, User.Role.MEMBER))
			.header(X_CLIENT_OS, "android")
			.header(X_CLIENT_OS_VERSION, "device_os_version")
			.header(X_DEVICE_ID, "device_id_3")
			.when()
			.get("/api/v1/accounts/me")
			.then()
			.statusCode(200);
	}

	@DisplayName("회원 탈퇴 API 문서화")
	@Test
	void withdraw() {
		RestAssured.given(spec)
			.filter(document("account/withdraw",
				authRequestHeaders(),
				relaxedResponseFields(
					fieldWithPath("result").description("결과 상태 (SUCCESS)"),
					fieldWithPath("data").description("응답 데이터 (null)"))))
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.header(AUTHORIZATION, AUTHENTICATION_TYPE + testTokenProvider.getAccessTokenValue())
			.header(X_CLIENT_OS, "ios")
			.header(X_CLIENT_OS_VERSION, "device_os_version")
			.header(X_DEVICE_ID, "device_id_1")
			.when()
			.delete("/api/v1/accounts/me")
			.then()
			.statusCode(200);
	}
}
