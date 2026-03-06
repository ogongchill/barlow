package com.barlow.app.api.controller.v1.legislationaccount;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import com.barlow.app.support.AcceptanceTest;
import com.barlow.app.support.RestDocsContextTest;
import com.barlow.core.enumerate.LegislationType;

@AcceptanceTest({"acceptance/legislationAccount.json", "acceptance/legislationAccountSubscribe.json"})
class LegislationAccountSubscribeControllerDocsTest extends RestDocsContextTest {

	@DisplayName("상임위원회 구독 활성화 API 문서화")
	@Test
	void activateSubscription() {
		givenWithAuth()
			.filter(document("legislation-accounts/subscribe/activate",
				authRequestHeaders(),
				pathParameters(
					parameterWithName("legislationType").description("구독할 상임위원회 유형")),
				relaxedResponseFields(
					fieldWithPath("result").description("결과 상태 (SUCCESS)"),
					fieldWithPath("data").description("응답 데이터 (null)"))))
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when()
			.post("/api/v1/legislation-accounts/{legislationType}/subscribe/activate",
				LegislationType.LEGISLATION_AND_JUDICIARY)
			.then()
			.statusCode(200);
	}

	@DisplayName("상임위원회 구독 비활성화 API 문서화")
	@Test
	void deactivateSubscription() {
		givenWithAuth()
			.filter(document("legislation-accounts/subscribe/deactivate",
				authRequestHeaders(),
				pathParameters(
					parameterWithName("legislationType").description("구독 해제할 상임위원회 유형")),
				relaxedResponseFields(
					fieldWithPath("result").description("결과 상태 (SUCCESS)"),
					fieldWithPath("data").description("응답 데이터 (null)"))))
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when()
			.post("/api/v1/legislation-accounts/{legislationType}/subscribe/deactivate",
				LegislationType.HOUSE_STEERING)
			.then()
			.statusCode(200);
	}
}
