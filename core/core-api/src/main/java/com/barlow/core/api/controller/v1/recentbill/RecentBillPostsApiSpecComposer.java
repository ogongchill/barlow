package com.barlow.core.api.controller.v1.recentbill;

import static com.barlow.core.api.controller.v1.recentbill.RecentBillPostsResponse.RecentBillPostThumbnail;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Predicate;

import com.barlow.core.domain.recentbill.RecentBillPost;
import com.barlow.core.domain.recentbill.RecentBillPostsStatus;

public class RecentBillPostsApiSpecComposer {

	private final RecentBillPostsStatus recentBillPostsStatus;

	public RecentBillPostsApiSpecComposer(RecentBillPostsStatus recentBillPostsStatus) {
		this.recentBillPostsStatus = recentBillPostsStatus;
	}

	RecentBillPostsResponse compose(LocalDate today) {
		return new RecentBillPostsResponse(
			composeByDatePredicate(recentBillPost -> recentBillPost.getCreatedAt().toLocalDate().isEqual(today)),
			composeByDatePredicate(recentBillPost -> recentBillPost.getCreatedAt().toLocalDate().isBefore(today)),
			recentBillPostsStatus.isLastPage()
		);
	}

	private List<RecentBillPostThumbnail> composeByDatePredicate(Predicate<RecentBillPost> filterCondition) {
		return recentBillPostsStatus.recentBillPosts()
			.stream()
			.filter(filterCondition)
			.map(recentBillPost -> new RecentBillPostThumbnail(
				recentBillPost.getBillName(),
				recentBillPost.getProposers(),
				recentBillPost.getLegislationProcessStatus(),
				recentBillPost.getViewCount()
			))
			.toList();
	}
}
