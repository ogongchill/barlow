package com.barlow.core.api.controller.v1.legislationaccount;

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

@AcceptanceTest({
	"acceptance/legislationAccount.json",
	"acceptance/legislationAccountSubscribe.json"})
@Import(TestTokenProvider.class)
class LegislationAccountSubscribeControllerTest extends ContextTest {

	@Autowired
	private TestTokenProvider testTokenProvider;

	@DisplayName("상임위원회 별 구독 활성화")
	@Nested
	class ActivateSubscription {

		@DisplayName("사용자가 상임위원회의 구독을 활성화하면 구독을 활성화시키고 성공한다")
		@Test
		void subscribe_success() {
			Map<String, Object> responseMap = activateSubscription(2L);
			assertAll(
				() -> assertThat(responseMap).containsEntry("result", ResultType.SUCCESS.name()),
				() -> assertThat(responseMap.get("data")).isNull(),
				() -> assertThat(responseMap.get("error")).isNull()
			);
		}

		@DisplayName("사용자가 이미 활성화된 상임위원회의 구독을 활성화하면 예외를 발생시키고 실패한다")
		@Test
		void subscribe_fail() {
			Map<String, Object> responseMap = activateSubscription(1L);
			assertThat(responseMap).containsEntry("result", ResultType.ERROR.name());
		}

		private Map<String, Object> activateSubscription(Long accountNo) {
			return RestAssured
				.given().log().all().contentType(MediaType.APPLICATION_JSON_VALUE)
				.headers(AUTHORIZATION, AUTHENTICATION_TYPE + testTokenProvider.getAccessTokenValue())
				.headers(MANDATORY_DEVICE_HEADERS)
				.when()
				.post("/api/v1/legislation-accounts/{accountNo}/subscribe/activate", accountNo)
				.then().log().all().extract()
				.jsonPath().getMap(".");
		}
	}

	@DisplayName("상임위원회 별 구독 비활성화")
	@Nested
	class DeactivateSubscription {

		@DisplayName("사용자가 상임위원회의 구독을 비활성화하면 구독을 비활성화시키고 성공한다")
		@Test
		void unsubscribe_success() {
			Map<String, Object> responseMap = deactivateSubscription(1L);
			assertAll(
				() -> assertThat(responseMap).containsEntry("result", ResultType.SUCCESS.name()),
				() -> assertThat(responseMap.get("data")).isNull(),
				() -> assertThat(responseMap.get("error")).isNull()
			);
		}

		@DisplayName("사용자가 이미 비활성화된 상임위원회의 구독을 비활성화하면 예외를 발생시키고 실패한다")
		@Test
		void unsubscribe_fail() {
			Map<String, Object> responseMap = deactivateSubscription(2L);
			assertThat(responseMap).containsEntry("result", ResultType.ERROR.name());
		}

		private Map<String, Object> deactivateSubscription(Long accountNo) {
			return RestAssured
				.given().log().all().contentType(MediaType.APPLICATION_JSON_VALUE)
				.headers(AUTHORIZATION, AUTHENTICATION_TYPE + testTokenProvider.getAccessTokenValue())
				.headers(MANDATORY_DEVICE_HEADERS)
				.when()
				.post("/api/v1/legislation-accounts/{accountNo}/subscribe/deactivate", accountNo)
				.then().log().all().extract()
				.jsonPath().getMap(".");
		}
	}
}