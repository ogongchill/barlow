package com.barlow.app.api.controller.v1.version;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.barlow.app.support.AcceptanceTest;
import com.barlow.app.support.RestDocsContextTest;
import com.barlow.test.api.RestDocUtils;

import io.restassured.RestAssured;

@AcceptanceTest("acceptance/clientVersion.json")
class ClientVersionCheckControllerDocsTest extends RestDocsContextTest {

	@DisplayName("클라이언트 버전 확인 API 문서화")
	@Test
	void checkClientVersion() {
		RestAssured.given(spec)
			.filter(document("client-version/check",
				RestDocUtils.requestPreprocessor(),
				RestDocUtils.responsePreprocessor(),
				requestHeaders(
					headerWithName("X-App-Version").description("클라이언트 앱 버전 (예: 1.5.0)"),
					headerWithName("X-Device-Os").description("디바이스 OS (ANDROID / IOS)")),
				relaxedResponseFields(
					fieldWithPath("result").description("결과 상태 (SUCCESS)"),
					fieldWithPath("data.status").description(
						"버전 상태 (UP_TO_DATE: 최신 / UPDATE_AVAILABLE: 업데이트 권장 / FORCE_UPDATE: 강제 업데이트 필요)"))))
			.header("X-App-Version", "1.5.0")
			.header("X-Device-Os", "ANDROID")
			.when()
			.get("/api/v1/client-version/check")
			.then()
			.statusCode(200);
	}
}
