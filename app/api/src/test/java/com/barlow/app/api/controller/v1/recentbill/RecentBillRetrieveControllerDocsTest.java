package com.barlow.app.api.controller.v1.recentbill;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import com.barlow.app.support.AcceptanceTest;
import com.barlow.app.support.RestDocsContextTest;

@AcceptanceTest("acceptance/billPost.json")
class RecentBillRetrieveControllerDocsTest extends RestDocsContextTest {

	@DisplayName("최근법안 게시글 목록 조회 API 문서화")
	@Test
	void retrieveRecentBills() {
		givenWithAuth()
			.filter(document("recent-bill/list",
				authRequestHeaders(),
				queryParameters(
					parameterWithName("page").description("페이지 번호 (0부터 시작)").optional(),
					parameterWithName("size").description("페이지 크기").optional()),
				relaxedResponseFields(
					fieldWithPath("result").description("결과 상태 (SUCCESS)"),
					subsectionWithPath("data").description("게시글 목록 및 페이지 정보"))))
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.queryParam("page", 0)
			.queryParam("size", 10)
			.when()
			.get("/api/v1/recent-bills")
			.then()
			.statusCode(200);
	}

	@DisplayName("최근법안 게시글 상세 조회 API 문서화")
	@Test
	void retrieveRecentBillDetail() {
		givenWithAuth()
			.filter(document("recent-bill/detail",
				authRequestHeaders(),
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
			.when()
			.get("/api/v1/recent-bills/{recentBillId}", "PRC_1")
			.then()
			.statusCode(200);
	}
}
