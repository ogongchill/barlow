package com.barlow.core.api.controller.v1.legislationaccount;

import static com.barlow.core.support.TestHttpUtils.*;
import static org.junit.jupiter.api.Assertions.*;
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

import io.restassured.RestAssured;

// fixme : tx 해결 후 다시 테스트
@AcceptanceTest({
	"acceptance/legislationAccount.json",
	"acceptance/legislationAccountSubscribe.json"})
@Import(TestTokenProvider.class)
class LegislationAccountSubscribeControllerTest extends ContextTest {

	@Autowired
	private TestTokenProvider testTokenProvider;

	@DisplayName("")
	@Nested
	class ActivateSubscription {

		@DisplayName("")
		@Test
		void subscribe() {
			Map<Object, Object> responseMap = RestAssured
				.given().log().all().contentType(MediaType.APPLICATION_JSON_VALUE)
				.headers(AUTHORIZATION, AUTHENTICATION_TYPE + testTokenProvider.getAccessTokenValue())
				.headers(MANDATORY_DEVICE_HEADERS)
				.when()
				.post("/api/v1/legislation-accounts/{accountNo}/subscribe/activate", 1)
				.then().log().all().extract()
				.jsonPath().getMap(".");
			assertAll(
				() -> assertThat(responseMap).containsEntry("result", "SUCCESS"),
				() -> assertThat(responseMap.get("data")).isNull(),
				() -> assertThat(responseMap.get("error")).isNull()
			);
		}
	}

	@DisplayName("")
	@Nested
	class DeactivateSubscription {

		@DisplayName("")
		@Test
		void unsubscribe() {

		}
	}
}