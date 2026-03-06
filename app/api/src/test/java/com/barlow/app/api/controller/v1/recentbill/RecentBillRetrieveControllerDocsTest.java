package com.barlow.app.api.controller.v1.recentbill;

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
class RecentBillRetrieveControllerDocsTest extends RestDocsContextTest {

	@Autowired
	private TestTokenProvider testTokenProvider;

	@DisplayName("최근법안 게시글 목록 조회 API 문서화")
	@Test
	void retrieveRecentBillThumbnail() {
		RestAssured.given(spec)
			.filter(document("recent-bill/thumbnail",
				RestDocUtils.requestPreprocessor(),
				RestDocUtils.responsePreprocessor(),
				requestHeaders(
					headerWithName(AUTHORIZATION).description("Bearer 액세스 토큰"),
					headerWithName("X-Client-OS").description("클라이언트 OS"),
					headerWithName("X-Client-OS-Version").description("클라이언트 OS 버전"),
					headerWithName("X-Device-ID").description("디바이스 ID")),
				queryParameters(
					parameterWithName("page").description("페이지 번호 (0부터 시작)").optional(),
					parameterWithName("size").description("페이지 크기").optional()),
				relaxedResponseFields(
					fieldWithPath("result").description("결과 상태 (SUCCESS)"),
					subsectionWithPath("data").description("게시글 목록 및 페이지 정보"))))
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.header(AUTHORIZATION, AUTHENTICATION_TYPE + testTokenProvider.getAccessTokenValue())
			.headers(MANDATORY_DEVICE_HEADERS)
			.queryParam("page", 0)
			.queryParam("size", 10)
			.when()
			.get("/api/v1/recent-bill/thumbnail")
			.then()
			.statusCode(200);
	}

	@DisplayName("최근법안 게시글 상세 조회 API 문서화")
	@Test
	void retrieveRecentBillDetail() {
		RestAssured.given(spec)
			.filter(document("recent-bill/detail",
				RestDocUtils.requestPreprocessor(),
				RestDocUtils.responsePreprocessor(),
				requestHeaders(
					headerWithName(AUTHORIZATION).description("Bearer 액세스 토큰"),
					headerWithName("X-Client-OS").description("클라이언트 OS"),
					headerWithName("X-Client-OS-Version").description("클라이언트 OS 버전"),
					headerWithName("X-Device-ID").description("디바이스 ID")),
				pathParameters(
					parameterWithName("recentBillId").description("조회할 법안 ID")),
				relaxedResponseFields(
					fieldWithPath("result").description("결과 상태 (SUCCESS)"),
					fieldWithPath("data.title").description("법안 제목"),
					fieldWithPath("data.proposerSummary").description("발의자 요약"),
					fieldWithPath("data.proposerType").description("발의 유형 (의원발의 / 정부발의 등)"),
					fieldWithPath("data.legislativeBody").description("입법 기관 유형"),
					subsectionWithPath("data.summarySection").description("AI 요약 정보"),
					subsectionWithPath("data.proposerSection").description("발의자 상세 정보"))))
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.header(AUTHORIZATION, AUTHENTICATION_TYPE + testTokenProvider.getAccessTokenValue())
			.headers(MANDATORY_DEVICE_HEADERS)
			.when()
			.get("/api/v1/recent-bill/detail/{recentBillId}", "PRC_1")
			.then()
			.statusCode(200);
	}
}
