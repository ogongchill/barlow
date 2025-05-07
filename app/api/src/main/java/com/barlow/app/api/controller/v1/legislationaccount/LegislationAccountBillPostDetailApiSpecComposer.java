package com.barlow.app.api.controller.v1.legislationaccount;

import static com.barlow.app.api.controller.v1.legislationaccount.LegislationAccountBillPostDetailResponse.ProposerResponse;
import static com.barlow.app.api.controller.v1.legislationaccount.LegislationAccountBillPostDetailResponse.ProposerSection;
import static com.barlow.app.api.controller.v1.legislationaccount.LegislationAccountBillPostDetailResponse.SummarySection;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.barlow.core.domain.billpost.BillPost;
import com.barlow.core.domain.billpost.BillProposer;

public class LegislationAccountBillPostDetailApiSpecComposer {

	private final BillPost billPost;

	LegislationAccountBillPostDetailApiSpecComposer(BillPost billPost) {
		this.billPost = billPost;
	}

	LegislationAccountBillPostDetailResponse compose() {
		BillProposers billProposers = new BillProposers(billPost.getBillProposers());
		return new LegislationAccountBillPostDetailResponse(
			billPost.getBillName(),
			billPost.getProposers(),
			billPost.getProposerType(),
			billPost.getLegislativeBody(),
			billPost.getCreatedAt().toLocalDate(),
			billPost.getDetail(),
			new SummarySection(billPost.getSummary()),
			new ProposerSection(
				billProposers.getProposerPartyRate(),
				billProposers.mapToProposerResponse()
			)
		);
	}

	static class BillProposers {

		private static final int COUNT_UNIT = 1;

		private final List<BillProposer> values;

		BillProposers(List<BillProposer> values) {
			this.values = values;
		}

		Map<String, Integer> getProposerPartyRate() {
			return values.stream()
				.collect(Collectors.groupingBy(
					BillProposer::getPartyName,
					Collectors.summingInt(value -> COUNT_UNIT)
				));
		}

		List<ProposerResponse> mapToProposerResponse() {
			return values.stream()
				.map(billProposer -> new ProposerResponse(
					billProposer.getProposerName(),
					billProposer.getProfileImagePath(),
					billProposer.getPartyName()
				))
				.toList();
		}
	}
}
