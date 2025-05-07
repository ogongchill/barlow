package com.barlow.app.api.controller.v1.legislationaccount;

import static com.barlow.app.api.controller.v1.legislationaccount.LegislationAccountBillPostsResponse.BillPostThumbnail;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Predicate;

import com.barlow.core.domain.billpost.BillPost;
import com.barlow.core.domain.billpost.BillPostsStatus;

public class LegislationAccountBillPostsApiSpecComposer {

	private final BillPostsStatus billPostsStatus;

	public LegislationAccountBillPostsApiSpecComposer(BillPostsStatus billPostsStatus) {
		this.billPostsStatus = billPostsStatus;
	}

	LegislationAccountBillPostsResponse compose(LocalDate today) {
		return new LegislationAccountBillPostsResponse(
			composeByDatePredicate(recentBillPost -> recentBillPost.getCreatedAt().toLocalDate().isEqual(today)),
			composeByDatePredicate(recentBillPost -> recentBillPost.getCreatedAt().toLocalDate().isBefore(today)),
			billPostsStatus.isLastPage()
		);
	}

	private List<BillPostThumbnail> composeByDatePredicate(Predicate<BillPost> filterCondition) {
		return billPostsStatus.billPosts()
			.stream()
			.filter(filterCondition)
			.map(recentBillPost -> new BillPostThumbnail(
				recentBillPost.getBillId(),
				recentBillPost.getBillName(),
				recentBillPost.getProposers(),
				recentBillPost.getLegislationProcessStatus()
			))
			.toList();
	}
}
