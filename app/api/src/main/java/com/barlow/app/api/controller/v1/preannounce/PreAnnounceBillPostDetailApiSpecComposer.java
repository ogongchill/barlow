package com.barlow.app.api.controller.v1.preannounce;

import static com.barlow.app.api.controller.v1.preannounce.PreAnnounceBillPostDetailResponse.PreAnnouncementSection;
import static com.barlow.app.api.controller.v1.preannounce.PreAnnounceBillPostDetailResponse.ProposerResponse;
import static com.barlow.app.api.controller.v1.preannounce.PreAnnounceBillPostDetailResponse.ProposerSection;
import static com.barlow.app.api.controller.v1.preannounce.PreAnnounceBillPostDetailResponse.SummarySection;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.barlow.core.domain.billpost.BillPost;
import com.barlow.core.domain.billpost.BillProposer;

public class PreAnnounceBillPostDetailApiSpecComposer {

	private final BillPost billPost;

	public PreAnnounceBillPostDetailApiSpecComposer(BillPost billPost) {
		this.billPost = billPost;
	}

	PreAnnounceBillPostDetailResponse compose(LocalDate now) {
		BillProposers billProposers = new BillProposers(billPost.getBillProposers());
		return new PreAnnounceBillPostDetailResponse(
			billPost.getBillName(),
			billPost.getProposers(),
			billPost.getLegislativeBody(),
			billPost.getDetail(),
			new PreAnnouncementSection(
				billPost.getPreAnnounceDeadline(),
				billPost.getPreAnnouncementUrl(),
				billPost.calculateDeadlineDay(now)
			),
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
					billProposer.getProposerCode(),
					billProposer.getProposerName(),
					billProposer.getProfileImagePath(),
					billProposer.getPartyName()
				))
				.toList();
		}
	}
}
