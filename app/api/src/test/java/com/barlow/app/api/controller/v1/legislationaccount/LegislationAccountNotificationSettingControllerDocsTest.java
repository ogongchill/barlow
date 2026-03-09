package com.barlow.app.api.controller.v1.legislationaccount;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;

import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import com.barlow.app.support.AcceptanceTest;
import com.barlow.app.support.RestDocsContextTest;
import com.barlow.core.enumerate.LegislationType;

@AcceptanceTest("acceptance/legislationAccountNotificationSetting.json")
class LegislationAccountNotificationSettingControllerDocsTest extends RestDocsContextTest {

	@DisplayName("상임위원회 알림설정 활성화 API 문서화")
	@Test
	void activateNotificationSetting() {
		givenWithAuth()
			.filter(document("legislation-accounts/notification-settings/activate",
				authRequestHeaders(),
				pathParameters(
					parameterWithName("legislationType").description("알림을 활성화할 상임위원회 유형")),
				requestFields(
					fieldWithPath("active").description("알림 활성화 여부 (true: 활성화, false: 비활성화)")),
				relaxedResponseFields(
					fieldWithPath("result").description("결과 상태 (SUCCESS)"),
					fieldWithPath("data").description("응답 데이터 (null)"))))
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(Map.of("active", true))
			.when()
			.patch("/api/v1/legislation-accounts/{legislationType}/notification-settings",
				LegislationType.HOUSE_STEERING)
			.then()
			.statusCode(200);
	}

	@DisplayName("상임위원회 알림설정 비활성화 API 문서화")
	@Test
	void deactivateNotificationSetting() {
		givenWithAuth()
			.filter(document("legislation-accounts/notification-settings/deactivate",
				authRequestHeaders(),
				pathParameters(
					parameterWithName("legislationType").description("알림을 비활성화할 상임위원회 유형")),
				requestFields(
					fieldWithPath("active").description("알림 활성화 여부 (true: 활성화, false: 비활성화)")),
				relaxedResponseFields(
					fieldWithPath("result").description("결과 상태 (SUCCESS)"),
					fieldWithPath("data").description("응답 데이터 (null)"))))
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(Map.of("active", false))
			.when()
			.patch("/api/v1/legislation-accounts/{legislationType}/notification-settings",
				LegislationType.LEGISLATION_AND_JUDICIARY)
			.then()
			.statusCode(200);
	}
}
