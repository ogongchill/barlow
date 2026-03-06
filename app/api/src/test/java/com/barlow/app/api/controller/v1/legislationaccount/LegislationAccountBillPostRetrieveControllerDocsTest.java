package com.barlow.app.api.controller.v1.legislationaccount;

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
import com.barlow.core.enumerate.LegislationType;

@AcceptanceTest("acceptance/billPost.json")
class LegislationAccountBillPostRetrieveControllerDocsTest extends RestDocsContextTest {

	@DisplayName("상임위원회 게시글 목록 조회 API 문서화")
	@Test
	void retrieveBillPosts() {
		givenWithAuth()
			.filter(document("legislation-accounts/bill-posts/list",
				authRequestHeaders(),
				pathParameters(
					parameterWithName("legislationType").description("상임위원회 유형 (예: HOUSE_STEERING)")),
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
			.get("/api/v1/legislation-accounts/{legislationType}/bill-posts", LegislationType.HOUSE_STEERING)
			.then()
			.statusCode(200);
	}

	@DisplayName("상임위원회 게시글 상세 조회 API 문서화")
	@Test
	void retrieveBillPostDetail() {
		givenWithAuth()
			.filter(document("legislation-accounts/bill-posts/detail",
				authRequestHeaders(),
				pathParameters(
					parameterWithName("billId").description("조회할 법안 ID")),
				relaxedResponseFields(
					fieldWithPath("result").description("결과 상태 (SUCCESS)"),
					fieldWithPath("data.title").description("법안 제목"),
					fieldWithPath("data.proposerSummary").description("발의자 요약"),
					subsectionWithPath("data.summarySection").description("AI 요약 정보"),
					subsectionWithPath("data.proposerSection").description("발의자 상세 정보"))))
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when()
			.get("/api/v1/legislation-accounts/bill-posts/{billId}", "PRC_1")
			.then()
			.statusCode(200);
	}
}
