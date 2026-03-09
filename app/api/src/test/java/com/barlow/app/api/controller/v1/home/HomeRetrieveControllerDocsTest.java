package com.barlow.app.api.controller.v1.home;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import com.barlow.app.support.AcceptanceTest;
import com.barlow.app.support.RestDocsContextTest;

@AcceptanceTest({"acceptance/legislationAccount.json", "acceptance/notificationCenter.json",
	"acceptance/legislationAccountSubscribe.json"})
class HomeRetrieveControllerDocsTest extends RestDocsContextTest {

	@DisplayName("메인 홈 조회 API 문서화")
	@Test
	void retrieveHome() {
		givenWithAuth()
			.filter(document("home/retrieve",
				authRequestHeaders(),
				relaxedResponseFields(
					fieldWithPath("result").description("결과 상태 (SUCCESS)"),
					subsectionWithPath("data.subscribeSection").description("구독한 상임위원회 섹션"),
					fieldWithPath("data.isNotificationArrived").description("오늘 수신된 알림 존재 여부"),
					subsectionWithPath("data.todayBillPostSection").description("오늘의 법안 섹션"))))
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when()
			.get("/api/v1/home")
			.then()
			.statusCode(200);
	}

	@DisplayName("알림센터 조회 API 문서화")
	@Test
	void retrieveNotificationCenter() {
		givenWithAuth()
			.filter(document("home/notification-center",
				authRequestHeaders(),
				relaxedResponseFields(
					fieldWithPath("result").description("결과 상태 (SUCCESS)"),
					subsectionWithPath("data.items").description("최근 알림 목록"))))
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when()
			.get("/api/v1/home/notification-center")
			.then()
			.statusCode(200);
	}
}
