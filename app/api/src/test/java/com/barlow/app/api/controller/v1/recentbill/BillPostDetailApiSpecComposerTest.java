package com.barlow.app.api.controller.v1.recentbill;

import static com.barlow.app.api.controller.v1.recentbill.RecentBillPostFixture.BILL_NAME_1;
import static com.barlow.app.api.controller.v1.recentbill.RecentBillPostFixture.DETAIL;
import static com.barlow.app.api.controller.v1.recentbill.RecentBillPostFixture.LEGISLATION_TYPE;
import static com.barlow.app.api.controller.v1.recentbill.RecentBillPostFixture.PARTY_NAME;
import static com.barlow.app.api.controller.v1.recentbill.RecentBillPostFixture.PROFILE_IMAGE_PATH;
import static com.barlow.app.api.controller.v1.recentbill.RecentBillPostFixture.PROPOSER;
import static com.barlow.app.api.controller.v1.recentbill.RecentBillPostFixture.PROPOSERS_SUMMARY;
import static com.barlow.app.api.controller.v1.recentbill.RecentBillPostFixture.PROPOSER_NAME;
import static com.barlow.app.api.controller.v1.recentbill.RecentBillPostFixture.PROPOSER_TYPE;
import static com.barlow.app.api.controller.v1.recentbill.RecentBillPostFixture.RECENT_BILL_POST;
import static com.barlow.app.api.controller.v1.recentbill.RecentBillPostFixture.SUMMARY;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BillPostDetailApiSpecComposerTest {


	@DisplayName("법안발의자 정보를 받아, 해당 발의자들의 정당명:의원수 비율을 반환한다")
	@Test
	void getProposerPartyRate() {
		RecentBillPostDetailApiSpecComposer.BillProposers billProposers
			= new RecentBillPostDetailApiSpecComposer.BillProposers(List.of(PROPOSER));

		Map<String, Integer> actual = billProposers.getProposerPartyRate();

		assertThat(actual).isEqualTo(Map.of(PARTY_NAME, 1));
	}

	@DisplayName("법안발의자 정보를 받아, 발의자 정보들을 응답 형식에 맞도록 매핑한다")
	@Test
	void mapToProposerResponse() {
		RecentBillPostDetailApiSpecComposer.BillProposers billProposers
			= new RecentBillPostDetailApiSpecComposer.BillProposers(List.of(PROPOSER));

		List<RecentBillPostDetailResponse.ProposerResponse> actual= billProposers.mapToProposerResponse();

		List<RecentBillPostDetailResponse.ProposerResponse> expect = List.of(
			new RecentBillPostDetailResponse.ProposerResponse(PROPOSER_NAME, PROFILE_IMAGE_PATH, PARTY_NAME)
		);
		assertThat(actual).isEqualTo(expect);
	}

	@DisplayName("최근법안게시글의 상세 내용을 받아 api 스펙에 맞게 구성한다")
	@Test
	void compose() {
		RECENT_BILL_POST.setBillProposers(List.of(PROPOSER));
		RecentBillPostDetailApiSpecComposer apiSpecComposer = new RecentBillPostDetailApiSpecComposer(RECENT_BILL_POST);

		RecentBillPostDetailResponse result = apiSpecComposer.compose();

		Map<String, Integer> expectProposerPartyRate = Map.of(PARTY_NAME, 1);
		List<RecentBillPostDetailResponse.ProposerResponse> expectProposerResponses = List.of(
			new RecentBillPostDetailResponse.ProposerResponse(PROPOSER_NAME, PROFILE_IMAGE_PATH, PARTY_NAME)
		);
		assertAll(
			() -> assertThat(result.title()).isEqualTo(BILL_NAME_1),
			() -> assertThat(result.proposerSummary()).isEqualTo(PROPOSERS_SUMMARY),
			() -> assertThat(result.proposerType()).isEqualTo(PROPOSER_TYPE.getValue()),
			() -> assertThat(result.legislativeBody()).isEqualTo(LEGISLATION_TYPE.getValue()),
			() -> assertThat(result.detail()).isEqualTo(DETAIL),
			() -> assertThat(result.summarySection().summaryDetail()).isEqualTo(SUMMARY),
			() -> assertThat(result.proposerSection().proposerPartyRate()).isEqualTo(expectProposerPartyRate),
			() -> assertThat(result.proposerSection().proposerResponses()).isEqualTo(expectProposerResponses)
		);
	}
}