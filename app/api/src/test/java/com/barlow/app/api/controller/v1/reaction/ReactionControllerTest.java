package com.barlow.app.api.controller.v1.reaction;

import static com.barlow.app.support.TestHttpUtils.AUTHENTICATION_TYPE;
import static com.barlow.app.support.TestHttpUtils.AUTHORIZATION;
import static com.barlow.app.support.TestHttpUtils.MANDATORY_DEVICE_HEADERS;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;

import com.barlow.app.support.AcceptanceTest;
import com.barlow.app.support.TestTokenProvider;
import com.barlow.app.support.response.ResultType;
import com.barlow.core.enumerate.ReactionTarget;
import com.barlow.core.enumerate.ReactionType;

import io.restassured.RestAssured;

@AcceptanceTest({
	"acceptance/billPost.json",
	"acceptance/reaction.json"})
@Import(TestTokenProvider.class)
class ReactionControllerTest {

	@Autowired
	private TestTokenProvider testTokenProvider;

	@DisplayName("법안 게시글의 리액션 종류와 개수 및 사용자의 리액션 여부를 조회한다")
	@Test
	void retrieveReaction() {
		Map<String, Object> responseMap = RestAssured
			.given().log().all().contentType(MediaType.APPLICATION_JSON_VALUE)
			.headers(AUTHORIZATION, AUTHENTICATION_TYPE + testTokenProvider.getAccessTokenValue())
			.headers(MANDATORY_DEVICE_HEADERS)
			.when()
			.queryParams("targetType", ReactionTarget.BILL_POST.name())
			.get("/api/v1/reactions/{targetId}", "PRC_1")
			.then().log().all().extract()
			.jsonPath().getMap(".");
		assertAll(
			() -> assertThat(responseMap).containsEntry("result", ResultType.SUCCESS.name()),
			() -> assertThat(responseMap.get("data")).isNotNull(),
			() -> assertThat(responseMap.get("error")).isNull()
		);
	}

	@DisplayName("리액션을 등록한다")
	@Nested
	class React {

		@DisplayName("사용자가 법안 게시글에 대한 리액션을 하면 리액션이 등록되고 성공한다")
		@Test
		void react_success() {
			Map<String, Object> responseMap = react("PRC_4", ReactionType.DISLIKE);
			assertAll(
				() -> assertThat(responseMap).containsEntry("result", ResultType.SUCCESS.name()),
				() -> assertThat(responseMap.get("data")).isNull(),
				() -> assertThat(responseMap.get("error")).isNull()
			);
		}

		@DisplayName("사용자가 이미 리액션한 법안 게시글에 대해 새로운 리액션을 하면 예외를 발생시키고 실패한다")
		@Test
		void react_failure() {
			Map<String, Object> responseMap = react("PRC_1", ReactionType.LIKE);
			assertThat(responseMap).containsEntry("result", ResultType.ERROR.name());
		}

		private Map<String, Object> react(String targetId, ReactionType reactionType) {
			return RestAssured
				.given().log().all().contentType(MediaType.APPLICATION_JSON_VALUE)
				.headers(AUTHORIZATION, AUTHENTICATION_TYPE + testTokenProvider.getAccessTokenValue())
				.headers(MANDATORY_DEVICE_HEADERS)
				.when()
				.queryParams(Map.of(
					"targetType", ReactionTarget.BILL_POST.name(),
					"reactionType", reactionType
				))
				.post("/api/v1/reactions/{targetId}", targetId)
				.then().log().all().extract()
				.jsonPath().getMap(".");
		}
	}

	@DisplayName("리액션을 해제한다")
	@Nested
	class RemoveReact {

		@DisplayName("사용자가 법안 게시글에 대해 동일한 리액션을 하면 리액션이 해제된다")
		@Test
		void reactionRemove() {
			Map<String, Object> responseMap = RestAssured
				.given().log().all().contentType(MediaType.APPLICATION_JSON_VALUE)
				.headers(AUTHORIZATION, AUTHENTICATION_TYPE + testTokenProvider.getAccessTokenValue())
				.headers(MANDATORY_DEVICE_HEADERS)
				.when()
				.queryParams(Map.of(
					"targetType", ReactionTarget.BILL_POST.name(),
					"reactionType", ReactionType.LIKE
				))
				.post("/api/v1/reactions/{targetId}/remove", "PRC_1")
				.then().log().all().extract()
				.jsonPath().getMap(".");
			assertAll(
				() -> assertThat(responseMap).containsEntry("result", ResultType.SUCCESS.name()),
				() -> assertThat(responseMap.get("data")).isNull(),
				() -> assertThat(responseMap.get("error")).isNull()
			);
		}
	}
}