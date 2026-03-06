package com.barlow.app.api.controller.v1.term;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.barlow.app.support.AcceptanceTest;
import com.barlow.app.support.RestDocsContextTest;
import com.barlow.test.api.RestDocUtils;

import io.restassured.RestAssured;

@AcceptanceTest("acceptance/term.json")
class TermControllerDocsTest extends RestDocsContextTest {

	@DisplayName("활성화된 약관 목록 조회 API 문서화")
	@Test
	void getActiveTerms() {
		RestAssured.given(spec)
			.filter(document("term/active",
				RestDocUtils.requestPreprocessor(),
				RestDocUtils.responsePreprocessor(),
				relaxedResponseFields(
					fieldWithPath("result").description("결과 상태 (SUCCESS)"),
					subsectionWithPath("data.activeTerms").description("활성화된 약관 목록"),
					fieldWithPath("data.activeTerms[].id").description("약관 ID"),
					fieldWithPath("data.activeTerms[].title").description("약관 제목"),
					fieldWithPath("data.activeTerms[].linkUrl").description("약관 상세 링크 URL"),
					fieldWithPath("data.activeTerms[].type").description("약관 유형 (SERVICE / PRIVACY / MARKETING)"),
					fieldWithPath("data.activeTerms[].version").description("약관 버전"),
					fieldWithPath("data.activeTerms[].required").description("필수 동의 여부"),
					fieldWithPath("data.activeTerms[].effectiveAt").description("약관 발효일시"))))
			.when()
			.get("/api/v1/term/active")
			.then()
			.statusCode(200);
	}
}
