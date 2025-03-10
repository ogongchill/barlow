package com.barlow.core.api.controller.v1.recentbill;

import static com.barlow.core.support.TestHttpUtils.AUTHENTICATION_TYPE;
import static com.barlow.core.support.TestHttpUtils.AUTHORIZATION;
import static com.barlow.core.support.TestHttpUtils.MANDATORY_DEVICE_HEADERS;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;

import com.barlow.core.ContextTest;
import com.barlow.core.support.AcceptanceTest;
import com.barlow.core.support.TestTokenProvider;
import com.barlow.core.support.response.ResultType;

import io.restassured.RestAssured;

@AcceptanceTest("acceptance/billPost.json")
@Import(TestTokenProvider.class)
class RecentBillRetrieveControllerTest extends ContextTest {

	@Autowired
	private TestTokenProvider testTokenProvider;

	@DisplayName("최근법안 게시글 전체 조회")
	@Nested
	class RetrieveBillPostThumbnailTest {

		@DisplayName("사용자가 최근법안 게시글을 전체 조회하면 게시글 전체 썸네일을 조회한다")
		@Test
		void retrieveRecentBill() {
			Map<String, Object> responseMap = RestAssured
				.given().log().all().contentType(MediaType.APPLICATION_JSON_VALUE)
				.headers(AUTHORIZATION, AUTHENTICATION_TYPE + testTokenProvider.getAccessTokenValue())
				.headers(MANDATORY_DEVICE_HEADERS)
				.when()
				.queryParams(Map.of(
					"page", 0,
					"size", 10
				))
				.get("/api/v1/recent-bill/thumbnail")
				.then().log().all().extract()
				.jsonPath().getMap(".");
			assertAll(
				() -> assertThat(responseMap).containsEntry("result", ResultType.SUCCESS.name()),
				() -> assertThat(responseMap.get("data")).isNotNull(),
				() -> assertThat(responseMap.get("error")).isNull()
			);
		}
	}

	@DisplayName("최근법안 게시글 상세조회")
	@Nested
	class RetrieveBillPostDetailTest {

		@DisplayName("사용자가 법안 id 로 최근법안 게시글을 상세 조회하면 게시글 상세 내용을 조회한다")
		@Test
		void retrieveRecentBillDetail_success() {
			Map<String, Object> responseMap = retrievePostDetail("PRC_1");
			assertAll(
				() -> assertThat(responseMap).containsEntry("result", ResultType.SUCCESS.name()),
				() -> assertThat(responseMap.get("data")).isNotNull(),
				() -> assertThat(responseMap.get("error")).isNull()
			);
		}

		@DisplayName("사용자가 존재하지 않는 법안 id 로 최근법안 게시글을 상세 조회하면 예외를 발생시킨다")
		@Test
		void retrieveRecentBillDetail_fail() {
			Map<String, Object> responseMap = retrievePostDetail("NONE");
			assertThat(responseMap).containsEntry("result", ResultType.ERROR.name());
		}

		private Map<String, Object> retrievePostDetail(String billId) {
			return RestAssured
				.given().log().all().contentType(MediaType.APPLICATION_JSON_VALUE)
				.headers(AUTHORIZATION, AUTHENTICATION_TYPE + testTokenProvider.getAccessTokenValue())
				.headers(MANDATORY_DEVICE_HEADERS)
				.when()
				.get("/api/v1/legislation-accounts/bill-posts/{billId}", billId)
				.then().log().all().extract()
				.jsonPath().getMap(".");
		}
	}
}