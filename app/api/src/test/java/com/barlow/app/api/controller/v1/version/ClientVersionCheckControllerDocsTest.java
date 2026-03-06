package com.barlow.app.api.controller.v1.version;

import static com.barlow.app.support.TestHttpUtils.AUTHENTICATION_TYPE;
import static com.barlow.app.support.TestHttpUtils.AUTHORIZATION;
import static com.barlow.app.support.TestHttpUtils.MANDATORY_DEVICE_HEADERS;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import com.barlow.app.support.AcceptanceTest;
import com.barlow.app.support.RestDocsContextTest;
import com.barlow.app.support.TestTokenProvider;
import com.barlow.test.api.RestDocUtils;

import io.restassured.RestAssured;

@AcceptanceTest({"acceptance/clientVersion.json", "acceptance/user.json", "acceptance/device.json"})
@Import(TestTokenProvider.class)
class ClientVersionCheckControllerDocsTest extends RestDocsContextTest {

	@Autowired
	private TestTokenProvider testTokenProvider;

	@DisplayName("클라이언트 버전 확인 API 문서화")
	@Test
	void checkClientVersion() {
		RestAssured.given(spec)
			.filter(document("client-version/check",
				RestDocUtils.requestPreprocessor(),
				RestDocUtils.responsePreprocessor(),
				requestHeaders(
					headerWithName(AUTHORIZATION).description("Bearer 액세스 토큰"),
					headerWithName("X-Client-OS").description("클라이언트 OS (ios / android)"),
					headerWithName("X-Client-OS-Version").description("클라이언트 OS 버전"),
					headerWithName("X-Device-ID").description("디바이스 고유 ID"),
					headerWithName("X-App-Version").description("클라이언트 앱 버전 (예: 1.5.0)"),
					headerWithName("X-Device-Os").description("디바이스 OS (ANDROID / IOS)")),
				relaxedResponseFields(
					fieldWithPath("result").description("결과 상태 (SUCCESS)"),
					fieldWithPath("data.needForceUpdate").description("강제 업데이트 필요 여부"),
					fieldWithPath("data.updateAvailable").description("업데이트 가능 여부"))))
			.header(AUTHORIZATION, AUTHENTICATION_TYPE + testTokenProvider.getAccessTokenValue())
			.headers(MANDATORY_DEVICE_HEADERS)
			.header("X-App-Version", "1.5.0")
			.header("X-Device-Os", "ANDROID")
			.when()
			.get("/api/v1/client-version/check")
			.then()
			.statusCode(200);
	}
}
