package com.barlow.core.api.controller.v1.preannounce;

import static com.barlow.core.api.controller.v1.preannounce.PreAnnounceBillPostsResponse.PreAnnounceBillThumbnail;

import java.time.LocalDate;
import java.util.List;

import com.barlow.core.domain.billpost.BillPostsStatus;

public class PreAnnounceBillPostsApiSpecComposer {

	private final BillPostsStatus billPostsStatus;

	public PreAnnounceBillPostsApiSpecComposer(BillPostsStatus billPostsStatus) {
		this.billPostsStatus = billPostsStatus;
	}

	PreAnnounceBillPostsResponse compose(LocalDate now) {
		List<PreAnnounceBillThumbnail> thumbnails = billPostsStatus.billPosts()
			.stream()
			.map(preAnnounceBillPost -> new PreAnnounceBillThumbnail(
				preAnnounceBillPost.getBillId(),
				preAnnounceBillPost.getBillName(),
				preAnnounceBillPost.getProposers(),
				preAnnounceBillPost.getLegislativeBody(),
				preAnnounceBillPost.calculateDeadlineDay(now)
			))
			.toList();
		return PreAnnounceBillPostsResponse.of(thumbnails, billPostsStatus.isLastPage());
	}
}
