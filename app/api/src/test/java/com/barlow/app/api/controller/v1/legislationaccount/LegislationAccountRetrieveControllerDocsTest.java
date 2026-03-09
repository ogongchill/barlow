package com.barlow.app.api.controller.v1.legislationaccount;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import com.barlow.app.support.AcceptanceTest;
import com.barlow.app.support.RestDocsContextTest;
import com.barlow.core.enumerate.LegislationType;

@AcceptanceTest({"acceptance/legislationAccount.json", "acceptance/legislationAccountNotificationSetting.json",
	"acceptance/legislationAccountSubscribe.json"})
class LegislationAccountRetrieveControllerDocsTest extends RestDocsContextTest {

	@DisplayName("상임위원회 프로필 조회 API 문서화")
	@Test
	void retrieveProfile() {
		givenWithAuth()
			.filter(document("legislation-accounts/profile",
				authRequestHeaders(),
				pathParameters(
					parameterWithName("legislationType").description("상임위원회 유형 (예: HOUSE_STEERING)")),
				relaxedResponseFields(
					fieldWithPath("result").description("결과 상태 (SUCCESS)"),
					fieldWithPath("data.accountName").description("상임위원회 이름"),
					fieldWithPath("data.iconUrl").description("아이콘 이미지 URL"),
					fieldWithPath("data.description").description("상임위원회 설명"),
					fieldWithPath("data.postCount").description("게시글 수"),
					fieldWithPath("data.subscriberCount").description("구독자 수"),
					fieldWithPath("data.isSubscribe").description("현재 사용자 구독 여부"),
					fieldWithPath("data.isNotifiable").description("현재 사용자 알림 활성화 여부"))))
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when()
			.get("/api/v1/legislation-accounts/{legislationType}/profile", LegislationType.HOUSE_STEERING)
			.then()
			.statusCode(200);
	}

	@DisplayName("전체 상임위원회 목록 조회 API 문서화")
	@Test
	void retrieveCommitteeAccounts() {
		givenWithAuth()
			.filter(document("legislation-accounts/committees",
				authRequestHeaders(),
				relaxedResponseFields(
					fieldWithPath("result").description("결과 상태 (SUCCESS)"),
					fieldWithPath("data.title").description("페이지 제목"),
					fieldWithPath("data.subtitle").description("페이지 부제목"),
					fieldWithPath("data.description").description("페이지 설명"),
					subsectionWithPath("data.accounts")
						.description("상임위원회 목록 (accountNo, accountName, iconUrl, isSubscribed, isNotifiable)"))))
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when()
			.get("/api/v1/legislation-accounts/committees/info")
			.then()
			.statusCode(200);
	}
}
