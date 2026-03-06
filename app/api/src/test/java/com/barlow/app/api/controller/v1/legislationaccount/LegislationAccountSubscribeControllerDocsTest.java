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

@AcceptanceTest({"acceptance/legislationAccount.json", "acceptance/legislationAccountSubscribe.json"})
@Import(TestTokenProvider.class)
class LegislationAccountSubscribeControllerDocsTest extends RestDocsContextTest {

	@Autowired
	private TestTokenProvider testTokenProvider;

	@DisplayName("상임위원회 구독 활성화 API 문서화")
	@Test
	void activateSubscription() {
		RestAssured.given(spec)
			.filter(document("legislation-accounts/subscribe/activate",
				RestDocUtils.requestPreprocessor(),
				RestDocUtils.responsePreprocessor(),
				requestHeaders(
					headerWithName(AUTHORIZATION).description("Bearer 액세스 토큰"),
					headerWithName("X-Client-OS").description("클라이언트 OS"),
					headerWithName("X-Client-OS-Version").description("클라이언트 OS 버전"),
					headerWithName("X-Device-ID").description("디바이스 ID")),
				pathParameters(
					parameterWithName("legislationType").description("구독할 상임위원회 유형")),
				relaxedResponseFields(
					fieldWithPath("result").description("결과 상태 (SUCCESS)"),
					fieldWithPath("data").description("응답 데이터 (null)"))))
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.header(AUTHORIZATION, AUTHENTICATION_TYPE + testTokenProvider.getAccessTokenValue())
			.headers(MANDATORY_DEVICE_HEADERS)
			.when()
			.post("/api/v1/legislation-accounts/{legislationType}/subscribe/activate",
				LegislationType.LEGISLATION_AND_JUDICIARY)
			.then()
			.statusCode(200);
	}

	@DisplayName("상임위원회 구독 비활성화 API 문서화")
	@Test
	void deactivateSubscription() {
		RestAssured.given(spec)
			.filter(document("legislation-accounts/subscribe/deactivate",
				RestDocUtils.requestPreprocessor(),
				RestDocUtils.responsePreprocessor(),
				requestHeaders(
					headerWithName(AUTHORIZATION).description("Bearer 액세스 토큰"),
					headerWithName("X-Client-OS").description("클라이언트 OS"),
					headerWithName("X-Client-OS-Version").description("클라이언트 OS 버전"),
					headerWithName("X-Device-ID").description("디바이스 ID")),
				pathParameters(
					parameterWithName("legislationType").description("구독 해제할 상임위원회 유형")),
				relaxedResponseFields(
					fieldWithPath("result").description("결과 상태 (SUCCESS)"),
					fieldWithPath("data").description("응답 데이터 (null)"))))
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.header(AUTHORIZATION, AUTHENTICATION_TYPE + testTokenProvider.getAccessTokenValue())
			.headers(MANDATORY_DEVICE_HEADERS)
			.when()
			.post("/api/v1/legislation-accounts/{legislationType}/subscribe/deactivate",
				LegislationType.HOUSE_STEERING)
			.then()
			.statusCode(200);
	}
}
