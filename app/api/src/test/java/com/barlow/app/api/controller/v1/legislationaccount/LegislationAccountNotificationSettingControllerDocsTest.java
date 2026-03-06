package com.barlow.app.api.controller.v1.legislationaccount;

import static com.barlow.app.support.TestHttpUtils.AUTHENTICATION_TYPE;
import static com.barlow.app.support.TestHttpUtils.AUTHORIZATION;
import static com.barlow.app.support.TestHttpUtils.MANDATORY_DEVICE_HEADERS;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;

import com.barlow.app.support.AcceptanceTest;
import com.barlow.app.support.RestDocsContextTest;
import com.barlow.app.support.TestTokenProvider;
import com.barlow.core.enumerate.LegislationType;
import com.barlow.test.api.RestDocUtils;

import io.restassured.RestAssured;

@AcceptanceTest("acceptance/legislationAccountNotificationSetting.json")
@Import(TestTokenProvider.class)
class LegislationAccountNotificationSettingControllerDocsTest extends RestDocsContextTest {

	@Autowired
	private TestTokenProvider testTokenProvider;

	@DisplayName("상임위원회 알림설정 활성화 API 문서화")
	@Test
	void activateNotificationSetting() {
		RestAssured.given(spec)
			.filter(document("legislation-accounts/notification-setting/activate",
				RestDocUtils.requestPreprocessor(),
				RestDocUtils.responsePreprocessor(),
				requestHeaders(
					headerWithName(AUTHORIZATION).description("Bearer 액세스 토큰"),
					headerWithName("X-Client-OS").description("클라이언트 OS"),
					headerWithName("X-Client-OS-Version").description("클라이언트 OS 버전"),
					headerWithName("X-Device-ID").description("디바이스 ID")),
				pathParameters(
					parameterWithName("legislationType").description("알림을 활성화할 상임위원회 유형")),
				relaxedResponseFields(
					fieldWithPath("result").description("결과 상태 (SUCCESS)"),
					fieldWithPath("data").description("응답 데이터 (null)"))))
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.header(AUTHORIZATION, AUTHENTICATION_TYPE + testTokenProvider.getAccessTokenValue())
			.headers(MANDATORY_DEVICE_HEADERS)
			.when()
			.pathParam("legislationType", LegislationType.HOUSE_STEERING)
			.post("/api/v1/legislation-accounts/{legislationType}/notification-setting/activate")
			.then()
			.statusCode(200);
	}

	@DisplayName("상임위원회 알림설정 비활성화 API 문서화")
	@Test
	void deactivateNotificationSetting() {
		RestAssured.given(spec)
			.filter(document("legislation-accounts/notification-setting/deactivate",
				RestDocUtils.requestPreprocessor(),
				RestDocUtils.responsePreprocessor(),
				requestHeaders(
					headerWithName(AUTHORIZATION).description("Bearer 액세스 토큰"),
					headerWithName("X-Client-OS").description("클라이언트 OS"),
					headerWithName("X-Client-OS-Version").description("클라이언트 OS 버전"),
					headerWithName("X-Device-ID").description("디바이스 ID")),
				pathParameters(
					parameterWithName("legislationType").description("알림을 비활성화할 상임위원회 유형")),
				relaxedResponseFields(
					fieldWithPath("result").description("결과 상태 (SUCCESS)"),
					fieldWithPath("data").description("응답 데이터 (null)"))))
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.header(AUTHORIZATION, AUTHENTICATION_TYPE + testTokenProvider.getAccessTokenValue())
			.headers(MANDATORY_DEVICE_HEADERS)
			.when()
			.pathParam("legislationType", LegislationType.LEGISLATION_AND_JUDICIARY)
			.post("/api/v1/legislation-accounts/{legislationType}/notification-setting/deactivate")
			.then()
			.statusCode(200);
	}
}
