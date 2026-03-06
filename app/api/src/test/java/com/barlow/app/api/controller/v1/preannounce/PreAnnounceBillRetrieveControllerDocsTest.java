package com.barlow.app.api.controller.v1.preannounce;

import static com.barlow.app.support.TestHttpUtils.AUTHENTICATION_TYPE;
import static com.barlow.app.support.TestHttpUtils.AUTHORIZATION;
import static com.barlow.app.support.TestHttpUtils.MANDATORY_DEVICE_HEADERS;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;

import com.barlow.app.support.AcceptanceTest;
import com.barlow.app.support.RestDocsContextTest;
import com.barlow.app.support.TestTokenProvider;
import com.barlow.test.api.RestDocUtils;

import io.restassured.RestAssured;

@AcceptanceTest("acceptance/billPost.json")
@Import(TestTokenProvider.class)
class PreAnnounceBillRetrieveControllerDocsTest extends RestDocsContextTest {

	@Autowired
	private TestTokenProvider testTokenProvider;

	@DisplayName("입법예고 게시글 목록 조회 API 문서화")
	@Test
	void retrievePreAnnouncementBills() {
		RestAssured.given(spec)
			.filter(document("pre-announcement-bills/list",
				RestDocUtils.requestPreprocessor(),
				RestDocUtils.responsePreprocessor(),
				requestHeaders(
					headerWithName(AUTHORIZATION).description("Bearer 액세스 토큰"),
					headerWithName("X-Client-OS").description("클라이언트 OS"),
					headerWithName("X-Client-OS-Version").description("클라이언트 OS 버전"),
					headerWithName("X-Device-ID").description("디바이스 ID")),
				queryParameters(
					parameterWithName("legislationType").description("상임위원회 유형 필터 (선택)").optional(),
					parameterWithName("partyName").description("정당명 필터 (선택)").optional()),
				relaxedResponseFields(
					fieldWithPath("result").description("결과 상태 (SUCCESS)"),
					subsectionWithPath("data").description("입법예고 게시글 목록"))))
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.header(AUTHORIZATION, AUTHENTICATION_TYPE + testTokenProvider.getAccessTokenValue())
			.headers(MANDATORY_DEVICE_HEADERS)
			.when()
			.get("/api/v1/pre-announcement-bills")
			.then()
			.statusCode(200);
	}

	@DisplayName("입법예고 게시글 상세 조회 API 문서화")
	@Test
	void retrievePreAnnouncementBillDetail() {
		RestAssured.given(spec)
			.filter(document("pre-announcement-bills/detail",
				RestDocUtils.requestPreprocessor(),
				RestDocUtils.responsePreprocessor(),
				requestHeaders(
					headerWithName(AUTHORIZATION).description("Bearer 액세스 토큰"),
					headerWithName("X-Client-OS").description("클라이언트 OS"),
					headerWithName("X-Client-OS-Version").description("클라이언트 OS 버전"),
					headerWithName("X-Device-ID").description("디바이스 ID")),
				pathParameters(
					parameterWithName("billId").description("조회할 법안 ID")),
				relaxedResponseFields(
					fieldWithPath("result").description("결과 상태 (SUCCESS)"),
					fieldWithPath("data.title").description("법안 제목"),
					fieldWithPath("data.proposerSummary").description("발의자 요약"),
					subsectionWithPath("data.preAnnouncementSection").description("입법예고 관련 정보 (기간, 링크 등)"),
					subsectionWithPath("data.summarySection").description("AI 요약 정보"),
					subsectionWithPath("data.proposerSection").description("발의자 상세 정보"))))
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.header(AUTHORIZATION, AUTHENTICATION_TYPE + testTokenProvider.getAccessTokenValue())
			.headers(MANDATORY_DEVICE_HEADERS)
			.when()
			.get("/api/v1/pre-announcement-bills/{billId}", "PRC_3")
			.then()
			.statusCode(200);
	}
}
