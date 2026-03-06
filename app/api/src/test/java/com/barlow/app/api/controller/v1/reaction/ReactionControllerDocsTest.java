package com.barlow.app.api.controller.v1.reaction;

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
import com.barlow.core.enumerate.ReactionTarget;
import com.barlow.core.enumerate.ReactionType;
import com.barlow.test.api.RestDocUtils;

import io.restassured.RestAssured;

@AcceptanceTest({"acceptance/billPost.json", "acceptance/reaction.json"})
@Import(TestTokenProvider.class)
class ReactionControllerDocsTest extends RestDocsContextTest {

	@Autowired
	private TestTokenProvider testTokenProvider;

	@DisplayName("리액션 조회 API 문서화")
	@Test
	void retrieveReaction() {
		RestAssured.given(spec)
			.filter(document("reactions/retrieve",
				RestDocUtils.requestPreprocessor(),
				RestDocUtils.responsePreprocessor(),
				requestHeaders(
					headerWithName(AUTHORIZATION).description("Bearer 액세스 토큰"),
					headerWithName("X-Client-OS").description("클라이언트 OS"),
					headerWithName("X-Client-OS-Version").description("클라이언트 OS 버전"),
					headerWithName("X-Device-ID").description("디바이스 ID")),
				pathParameters(
					parameterWithName("targetId").description("리액션 대상 ID (법안 ID 등)")),
				queryParameters(
					parameterWithName("targetType").description("리액션 대상 유형 (예: BILL_POST)")),
				relaxedResponseFields(
					fieldWithPath("result").description("결과 상태 (SUCCESS)"),
					subsectionWithPath("data.reactions").description("리액션 종류별 개수 (LIKE, DISLIKE 등)"),
					subsectionWithPath("data.status").description("현재 사용자의 리액션 상태 (reactionType, hasReacted)"))))
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.header(AUTHORIZATION, AUTHENTICATION_TYPE + testTokenProvider.getAccessTokenValue())
			.headers(MANDATORY_DEVICE_HEADERS)
			.queryParam("targetType", ReactionTarget.BILL_POST.name())
			.when()
			.get("/api/v1/reactions/{targetId}", "PRC_1")
			.then()
			.statusCode(200);
	}

	@DisplayName("리액션 등록 API 문서화")
	@Test
	void react() {
		RestAssured.given(spec)
			.filter(document("reactions/react",
				RestDocUtils.requestPreprocessor(),
				RestDocUtils.responsePreprocessor(),
				requestHeaders(
					headerWithName(AUTHORIZATION).description("Bearer 액세스 토큰"),
					headerWithName("X-Client-OS").description("클라이언트 OS"),
					headerWithName("X-Client-OS-Version").description("클라이언트 OS 버전"),
					headerWithName("X-Device-ID").description("디바이스 ID")),
				pathParameters(
					parameterWithName("targetId").description("리액션 대상 ID")),
				queryParameters(
					parameterWithName("targetType").description("리액션 대상 유형 (예: BILL_POST)"),
					parameterWithName("reactionType").description("리액션 종류 (LIKE / DISLIKE)")),
				relaxedResponseFields(
					fieldWithPath("result").description("결과 상태 (SUCCESS)"),
					fieldWithPath("data").description("응답 데이터 (null)"))))
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.header(AUTHORIZATION, AUTHENTICATION_TYPE + testTokenProvider.getAccessTokenValue())
			.headers(MANDATORY_DEVICE_HEADERS)
			.queryParam("targetType", ReactionTarget.BILL_POST.name())
			.queryParam("reactionType", ReactionType.DISLIKE.name())
			.when()
			.post("/api/v1/reactions/{targetId}", "PRC_4")
			.then()
			.statusCode(200);
	}

	@DisplayName("리액션 해제 API 문서화")
	@Test
	void removeReaction() {
		RestAssured.given(spec)
			.filter(document("reactions/remove",
				RestDocUtils.requestPreprocessor(),
				RestDocUtils.responsePreprocessor(),
				requestHeaders(
					headerWithName(AUTHORIZATION).description("Bearer 액세스 토큰"),
					headerWithName("X-Client-OS").description("클라이언트 OS"),
					headerWithName("X-Client-OS-Version").description("클라이언트 OS 버전"),
					headerWithName("X-Device-ID").description("디바이스 ID")),
				pathParameters(
					parameterWithName("targetId").description("리액션 해제 대상 ID")),
				queryParameters(
					parameterWithName("targetType").description("리액션 대상 유형 (예: BILL_POST)"),
					parameterWithName("reactionType").description("해제할 리액션 종류 (LIKE / DISLIKE)")),
				relaxedResponseFields(
					fieldWithPath("result").description("결과 상태 (SUCCESS)"),
					fieldWithPath("data").description("응답 데이터 (null)"))))
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.header(AUTHORIZATION, AUTHENTICATION_TYPE + testTokenProvider.getAccessTokenValue())
			.headers(MANDATORY_DEVICE_HEADERS)
			.queryParam("targetType", ReactionTarget.BILL_POST.name())
			.queryParam("reactionType", ReactionType.LIKE.name())
			.when()
			.post("/api/v1/reactions/{targetId}/remove", "PRC_1")
			.then()
			.statusCode(200);
	}
}
