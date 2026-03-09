package com.barlow.app.api.controller.v1.preannounce;

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
class PreAnnounceBillRetrieveControllerDocsTest extends RestDocsContextTest {

	@DisplayName("입법예고 게시글 목록 조회 API 문서화")
	@Test
	void retrievePreAnnouncementBills() {
		givenWithAuth()
			.filter(document("pre-announcement-bills/list",
				authRequestHeaders(),
				queryParameters(
					parameterWithName("legislationType").description("상임위원회 유형 필터 (선택)").optional(),
					parameterWithName("partyName").description("정당명 필터 (선택)").optional()),
				relaxedResponseFields(
					fieldWithPath("result").description("결과 상태 (SUCCESS)"),
					subsectionWithPath("data").description("입법예고 게시글 목록"))))
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when()
			.get("/api/v1/pre-announcement-bills")
			.then()
			.statusCode(200);
	}

	@DisplayName("입법예고 게시글 상세 조회 API 문서화")
	@Test
	void retrievePreAnnouncementBillDetail() {
		givenWithAuth()
			.filter(document("pre-announcement-bills/detail",
				authRequestHeaders(),
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
			.when()
			.get("/api/v1/pre-announcement-bills/{billId}", "PRC_3")
			.then()
			.statusCode(200);
	}
}
