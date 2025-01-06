package com.barlow.core.api.controller.v1.recentbill;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.barlow.core.domain.recentbill.BillProposer;
import com.barlow.core.domain.recentbill.RecentBillPost;

public class RecentBillPostDetailApiSpecComposer {

	private final RecentBillPost recentBillPost;

	RecentBillPostDetailApiSpecComposer(RecentBillPost recentBillPost) {
		this.recentBillPost = recentBillPost;
	}

	RecentBillPostDetailResponse compose() {
		BillProposers billProposers = new BillProposers(recentBillPost.getBillProposers());
		return new RecentBillPostDetailResponse(
			recentBillPost.getBillName(),
			recentBillPost.getProposers(),
			recentBillPost.getCreatedAt(),
			recentBillPost.getViewCount(),
			recentBillPost.getDetail(),
			new RecentBillPostDetailResponse.SummarySection(recentBillPost.getSummary()),
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
