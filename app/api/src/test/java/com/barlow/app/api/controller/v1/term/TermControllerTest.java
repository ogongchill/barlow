package com.barlow.app.api.controller.v1.term;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.barlow.ContextTest;
import com.barlow.app.support.AcceptanceTest;
import com.barlow.app.support.response.ResultType;

import io.restassured.RestAssured;

@AcceptanceTest({"acceptance/term.json"})
class TermControllerTest extends ContextTest {

	@DisplayName("활성화된 약관 목록을 조회한다")
	@Test
	void getActiveTerms_success() {
		// when
		Map<String, Object> responseMap = RestAssured.given().log().all()
			.when()
			.get("/api/v1/term/active")
			.then().log().all().extract()
			.jsonPath().getMap(".");

		// then
		assertAll(
			() -> assertThat(responseMap).containsEntry("result", ResultType.SUCCESS.name()),
			() -> assertThat(responseMap.get("data")).isNotNull(),
			() -> assertThat(responseMap.get("error")).isNull()
		);

		// then - 응답 데이터 검증
		Map<String, Object> data = (Map<String, Object>) responseMap.get("data");
		List<Map<String, Object>> activeTerms = (List<Map<String, Object>>) data.get("activeTerms");

		assertAll(
			() -> assertThat(activeTerms).hasSize(3),
			() -> assertThat(activeTerms).extracting("title")
				.containsExactlyInAnyOrder("서비스 이용약관", "개인정보 처리방침", "마케팅 정보 수신 동의"),
			() -> assertThat(activeTerms).extracting("type")
				.containsExactlyInAnyOrder("SERVICE", "PRIVACY", "MARKETING"),
			() -> assertThat(activeTerms).filteredOn(term -> (boolean) term.get("required"))
				.hasSize(2)
		);
	}
}
