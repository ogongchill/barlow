package com.barlow.core.api.controller.v1.preannounce;

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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.barlow.core.ContextTest;
import com.barlow.core.support.AcceptanceTest;
import com.barlow.core.support.TestTokenProvider;

import io.restassured.RestAssured;

@AcceptanceTest("acceptance/billPost.json")
@Import(TestTokenProvider.class)
class PreAnnounceBillRetrieveControllerTest extends ContextTest {

	@Autowired
	private TestTokenProvider testTokenProvider;

	@DisplayName("진행중인 입법예고 게시글 전체조회")
	@Nested
	class RetrieveBillPosts {

		@DisplayName("사용자가 진행중인 입법예고 법안 게시글 전체를 tag 없이 기본 조회한다")
		@Test
		void retrievePreAnnouncementBills_withoutTags() {
			Map<String, Object> responseMap = RestAssured
				.given().log().all().contentType(MediaType.APPLICATION_JSON_VALUE)
				.headers(AUTHORIZATION, AUTHENTICATION_TYPE + testTokenProvider.getAccessTokenValue())
				.headers(MANDATORY_DEVICE_HEADERS)
				.when()
				.get("/api/v1/pre-announcement-bills")
				.then().log().all().extract()
				.jsonPath().getMap(".");
			assertAll(
				() -> assertThat(responseMap).containsEntry("result", "SUCCESS"),
				() -> assertThat(responseMap.get("data")).isNotNull(),
				() -> assertThat(responseMap.get("error")).isNull()
			);
		}

		@DisplayName("사용자가 진행중인 입법예고 법안 게시글 전체를 tag 와 함께 조회한다")
		@Test
		void retrievePreAnnouncementBills_withTags() {
			MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
			queryParams.set("legislationType", "HOUSE_STEERING");
			queryParams.set("partyName", "PEOPLE_POWER");
			Map<String, Object> responseMap = RestAssured
				.given().log().all().contentType(MediaType.APPLICATION_JSON_VALUE)
				.headers(AUTHORIZATION, AUTHENTICATION_TYPE + testTokenProvider.getAccessTokenValue())
				.headers(MANDATORY_DEVICE_HEADERS)
				.when()
				.queryParams(queryParams)
				.get("/api/v1/pre-announcement-bills")
				.then().log().all().extract()
				.jsonPath().getMap(".");
			assertAll(
				() -> assertThat(responseMap).containsEntry("result", "SUCCESS"),
				() -> assertThat(responseMap.get("data")).isNotNull(),
				() -> assertThat(responseMap.get("error")).isNull()
			);
		}
	}

	@DisplayName("진행중인 입법예고 게시글 상세조회")
	@Nested
	class RetrieveBillPostWithPreAnnouncement {

		@DisplayName("사용자가 법안 id 로 진행중인 입법예고 게시글을 상세 조회하면 게시글 상세 내용을 조회한다")
		@Test
		void retrieveBillPostDetail_success() {
			Map<String, Object> responseMap = retrievePreAnnounceBillPostDetail("PRC_3");

			assertAll(
				() -> assertThat(responseMap).containsEntry("result", "SUCCESS"),
				() -> assertThat(responseMap.get("data")).isNotNull(),
				() -> assertThat(responseMap.get("error")).isNull()
			);
		}

		@DisplayName("사용자가 존재하지 않는 법안 id 로 진행중인 입법예고 게시글을 상세 조회하면 예외를 발생시킨다")
		@Test
		void retrieveBillPostDetail_fail() {
			Map<String, Object> responseMap = retrievePreAnnounceBillPostDetail("NONE");
			assertThat(responseMap).containsEntry("result", "ERROR");
		}

		private Map<String, Object> retrievePreAnnounceBillPostDetail(String billId) {
			return RestAssured
				.given().log().all().contentType(MediaType.APPLICATION_JSON_VALUE)
				.headers(AUTHORIZATION, AUTHENTICATION_TYPE + testTokenProvider.getAccessTokenValue())
				.headers(MANDATORY_DEVICE_HEADERS)
				.when()
				.get("/api/v1/pre-announcement-bills/{billId}", billId)
				.then().log().all().extract()
				.jsonPath().getMap(".");
		}
	}
}