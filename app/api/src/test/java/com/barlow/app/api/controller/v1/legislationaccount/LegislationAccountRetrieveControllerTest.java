package com.barlow.app.api.controller.v1.legislationaccount;

import static com.barlow.core.support.TestHttpUtils.AUTHENTICATION_TYPE;
import static com.barlow.core.support.TestHttpUtils.AUTHORIZATION;
import static com.barlow.core.support.TestHttpUtils.MANDATORY_DEVICE_HEADERS;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;

import com.barlow.core.ContextTest;
import com.barlow.core.enumerate.LegislationType;
import com.barlow.core.support.AcceptanceTest;
import com.barlow.core.support.TestTokenProvider;
import com.barlow.core.support.response.ResultType;

import io.restassured.RestAssured;

@AcceptanceTest({
	"acceptance/legislationAccount.json",
	"acceptance/legislationAccountNotificationSetting.json",
	"acceptance/legislationAccountSubscribe.json"})
@Import(TestTokenProvider.class)
class LegislationAccountRetrieveControllerTest extends ContextTest {

	@Autowired
	private TestTokenProvider testTokenProvider;

	@DisplayName("사용자가 상임위원회 전체 페이지를 조회하면 상임위원회 별 사용자의 구독 정보 및 알림정보를 반환한다")
	@Test
	void retrieveCommitteeAccounts() {
		Map<String, Object> responseMap = RestAssured
			.given().log().all().contentType(MediaType.APPLICATION_JSON_VALUE)
			.headers(AUTHORIZATION, AUTHENTICATION_TYPE + testTokenProvider.getAccessTokenValue())
			.headers(MANDATORY_DEVICE_HEADERS)
			.when()
			.get("/api/v1/legislation-accounts/committees/info")
			.then().log().all().extract()
			.jsonPath().getMap(".");
		assertAll(
			() -> assertThat(responseMap).containsEntry("result", ResultType.SUCCESS.name()),
			() -> assertThat(responseMap.get("data")).isNotNull(),
			() -> assertThat(responseMap.get("error")).isNull()
		);
	}

	@DisplayName("사용자가 accountNo 를 통해 특정 상임위원회 페이지를 조회하면 해당 상임위원회에 대한 상세 정보 및 구독,알림설정 정보를 반환한다")
	@Test
	void retrieveProfile() {
		Map<String, Object> responseMap = RestAssured
			.given().log().all().contentType(MediaType.APPLICATION_JSON_VALUE)
			.headers(AUTHORIZATION, AUTHENTICATION_TYPE + testTokenProvider.getAccessTokenValue())
			.headers(MANDATORY_DEVICE_HEADERS)
			.when()
			.get("/api/v1/legislation-accounts/{legislationType}/profile", LegislationType.HOUSE_STEERING)
			.then().log().all().extract()
			.jsonPath().getMap(".");
		assertAll(
			() -> assertThat(responseMap).containsEntry("result", ResultType.SUCCESS.name()),
			() -> assertThat(responseMap.get("data")).isNotNull(),
			() -> assertThat(responseMap.get("error")).isNull()
		);
	}
}