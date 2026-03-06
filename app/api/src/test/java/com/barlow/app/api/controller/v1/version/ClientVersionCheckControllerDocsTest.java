package com.barlow.app.api.controller.v1.version;

import static com.barlow.app.support.TestHttpUtils.AUTHORIZATION;
import static com.barlow.app.support.TestHttpUtils.X_CLIENT_OS;
import static com.barlow.app.support.TestHttpUtils.X_CLIENT_OS_VERSION;
import static com.barlow.app.support.TestHttpUtils.X_DEVICE_ID;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.barlow.app.support.AcceptanceTest;
import com.barlow.app.support.RestDocsContextTest;

@AcceptanceTest({"acceptance/clientVersion.json", "acceptance/user.json", "acceptance/device.json"})
class ClientVersionCheckControllerDocsTest extends RestDocsContextTest {

	@DisplayName("클라이언트 버전 확인 API 문서화")
	@Test
	void checkClientVersion() {
		givenWithAuth()
			.filter(document("client-version/check",
				requestHeaders(
					headerWithName(AUTHORIZATION).description("Bearer 액세스 토큰"),
					headerWithName(X_CLIENT_OS).description("클라이언트 OS (ios / android)"),
					headerWithName(X_CLIENT_OS_VERSION).description("클라이언트 OS 버전"),
					headerWithName(X_DEVICE_ID).description("디바이스 고유 ID"),
					headerWithName("X-App-Version").description("클라이언트 앱 버전 (예: 1.5.0)"),
					headerWithName("X-Device-Os").description("디바이스 OS (ANDROID / IOS)")),
				relaxedResponseFields(
					fieldWithPath("result").description("결과 상태 (SUCCESS)"),
					fieldWithPath("data.needForceUpdate").description("강제 업데이트 필요 여부"),
					fieldWithPath("data.updateAvailable").description("업데이트 가능 여부"))))
			.header("X-App-Version", "1.5.0")
			.header("X-Device-Os", "ANDROID")
			.when()
			.get("/api/v1/client-version/check")
			.then()
			.statusCode(200);
	}
}
