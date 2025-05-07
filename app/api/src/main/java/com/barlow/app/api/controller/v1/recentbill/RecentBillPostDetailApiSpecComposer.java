package com.barlow.app.api.controller.v1.recentbill;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.barlow.core.domain.billpost.BillProposer;
import com.barlow.core.domain.billpost.BillPost;

public class RecentBillPostDetailApiSpecComposer {

	private final BillPost billPost;

	RecentBillPostDetailApiSpecComposer(BillPost billPost) {
		this.billPost = billPost;
	}

	RecentBillPostDetailResponse compose() {
		BillProposers billProposers = new BillProposers(billPost.getBillProposers());
		return new RecentBillPostDetailResponse(
			billPost.getBillName(),
			billPost.getProposers(),
			billPost.getProposerType(),
			billPost.getLegislativeBody(),
			billPost.getCreatedAt().toLocalDate(),
			billPost.getDetail(),
			new RecentBillPostDetailResponse.SummarySection(billPost.getSummary()),
			new RecentBillPostDetailResponse.ProposerSection(
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

		List<RecentBillPostDetailResponse.ProposerResponse> mapToProposerResponse() {
			return values.stream()
				.map(billProposer -> new RecentBillPostDetailResponse.ProposerResponse(
					billProposer.getProposerName(),
					billProposer.getProfileImagePath(),
					billProposer.getPartyName()
				))
				.toList();
		}
	}
}
