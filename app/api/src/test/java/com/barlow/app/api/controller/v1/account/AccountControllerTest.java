package com.barlow.app.api.controller.v1.account;

import static com.barlow.app.support.TestHttpUtils.AUTHENTICATION_TYPE;
import static com.barlow.app.support.TestHttpUtils.AUTHORIZATION;
import static com.barlow.app.support.TestHttpUtils.MANDATORY_DEVICE_HEADERS;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;

import com.barlow.ContextTest;
import com.barlow.app.support.AcceptanceTest;
import com.barlow.app.support.TestTokenProvider;
import com.barlow.app.support.response.ResultType;

import io.restassured.RestAssured;

@AcceptanceTest({
	"acceptance/user.json",
	"acceptance/device.json",
	"acceptance/notificationCenter.json",
	"acceptance/legislationAccount.json",
	"acceptance/legislationAccountNotificationSetting.json",
	"acceptance/legislationAccountSubscribe.json"
})
@Import(TestTokenProvider.class)
class AccountControllerTest extends ContextTest {

	@Autowired
	private TestTokenProvider testTokenProvider;

	@DisplayName("사용자가 게스트 회원가입을 하면 회원가입 절차를 진행하고 access token 을 반환한다")
	@Test
	void withdraw() {
		Map<String, Object> responseMap = RestAssured
			.given().log().all().contentType(MediaType.APPLICATION_JSON_VALUE)
			.headers(AUTHORIZATION, AUTHENTICATION_TYPE + testTokenProvider.getAccessTokenValue())
			.headers(MANDATORY_DEVICE_HEADERS)
			.when()
			.post("/api/v1/account/withdraw")
			.then().log().all().extract()
			.jsonPath().getMap(".");
		assertAll(
			() -> assertThat(responseMap).containsEntry("result", ResultType.SUCCESS.name()),
			() -> assertThat(responseMap.get("data")).isNull(),
			() -> assertThat(responseMap.get("error")).isNull()
		);
	}
}