package com.barlow.app.api.controller.v1.reaction;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;

import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import com.barlow.app.support.AcceptanceTest;
import com.barlow.app.support.RestDocsContextTest;
import com.barlow.core.enumerate.ReactionTarget;
import com.barlow.core.enumerate.ReactionType;

@AcceptanceTest({"acceptance/billPost.json", "acceptance/reaction.json"})
class ReactionControllerDocsTest extends RestDocsContextTest {

	@DisplayName("리액션 조회 API 문서화")
	@Test
	void retrieveReaction() {
		givenWithAuth()
			.filter(document("reactions/retrieve",
				authRequestHeaders(),
				pathParameters(
					parameterWithName("targetId").description("리액션 대상 ID (법안 ID 등)")),
				queryParameters(
					parameterWithName("targetType").description("리액션 대상 유형 (예: BILL_POST)")),
				relaxedResponseFields(
					fieldWithPath("result").description("결과 상태 (SUCCESS)"),
					subsectionWithPath("data.reactions").description("리액션 종류별 개수 (LIKE, DISLIKE 등)"),
					subsectionWithPath("data.status").description("현재 사용자의 리액션 상태 (reactionType, hasReacted)"))))
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.queryParam("targetType", ReactionTarget.BILL_POST.name())
			.when()
			.get("/api/v1/reactions/{targetId}", "PRC_1")
			.then()
			.statusCode(200);
	}

	@DisplayName("리액션 등록 API 문서화")
	@Test
	void react() {
		givenWithAuth()
			.filter(document("reactions/react",
				authRequestHeaders(),
				pathParameters(
					parameterWithName("targetId").description("리액션 대상 ID")),
				requestFields(
					fieldWithPath("targetType").description("리액션 대상 유형 (예: BILL_POST)"),
					fieldWithPath("reactionType").description("리액션 종류 (LIKE / DISLIKE / HMM)")),
				relaxedResponseFields(
					fieldWithPath("result").description("결과 상태 (SUCCESS)"),
					fieldWithPath("data").description("응답 데이터 (null)"))))
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(Map.of(
				"targetType", ReactionTarget.BILL_POST.name(),
				"reactionType", ReactionType.DISLIKE.name()))
			.when()
			.post("/api/v1/reactions/{targetId}", "PRC_4")
			.then()
			.statusCode(201);
	}

	@DisplayName("리액션 해제 API 문서화")
	@Test
	void removeReaction() {
		givenWithAuth()
			.filter(document("reactions/remove",
				authRequestHeaders(),
				pathParameters(
					parameterWithName("targetId").description("리액션 해제 대상 ID")),
				queryParameters(
					parameterWithName("targetType").description("리액션 대상 유형 (예: BILL_POST)"),
					parameterWithName("reactionType").description("해제할 리액션 종류 (LIKE / DISLIKE / HMM)")),
				relaxedResponseFields(
					fieldWithPath("result").description("결과 상태 (SUCCESS)"),
					fieldWithPath("data").description("응답 데이터 (null)"))))
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.queryParam("targetType", ReactionTarget.BILL_POST.name())
			.queryParam("reactionType", ReactionType.LIKE.name())
			.when()
			.delete("/api/v1/reactions/{targetId}", "PRC_1")
			.then()
			.statusCode(200);
	}
}
