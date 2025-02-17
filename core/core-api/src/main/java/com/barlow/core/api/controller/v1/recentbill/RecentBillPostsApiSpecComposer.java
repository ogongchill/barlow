package com.barlow.core.api.controller.v1.recentbill;

import static com.barlow.core.api.controller.v1.recentbill.RecentBillPostsResponse.RecentBillPostThumbnail;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Predicate;

import com.barlow.core.domain.recentbill.BillPost;
import com.barlow.core.domain.recentbill.BillPostsStatus;

public class RecentBillPostsApiSpecComposer {

	private final BillPostsStatus billPostsStatus;

	public RecentBillPostsApiSpecComposer(BillPostsStatus billPostsStatus) {
		this.billPostsStatus = billPostsStatus;
	}

	RecentBillPostsResponse compose(LocalDate today) {
		return new RecentBillPostsResponse(
			composeByDatePredicate(recentBillPost -> recentBillPost.getCreatedAt().toLocalDate().isEqual(today)),
			composeByDatePredicate(recentBillPost -> recentBillPost.getCreatedAt().toLocalDate().isBefore(today)),
			billPostsStatus.isLastPage()
		);
	}

	private List<RecentBillPostThumbnail> composeByDatePredicate(Predicate<BillPost> filterCondition) {
		return billPostsStatus.billPosts()
			.stream()
			.filter(filterCondition)
			.map(recentBillPost -> new RecentBillPostThumbnail(
				recentBillPost.getBillId(),
				recentBillPost.getBillName(),
				recentBillPost.getProposers(),
				recentBillPost.getLegislationProcessStatus()
			))
			.toList();
	}
}
