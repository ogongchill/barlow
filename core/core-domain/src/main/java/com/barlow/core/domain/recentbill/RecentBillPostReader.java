package com.barlow.core.domain.recentbill;

import org.springframework.stereotype.Component;

@Component
public class RecentBillPostReader {

	private final RecentBillPostRepository recentBillPostRepository;

	public RecentBillPostReader(RecentBillPostRepository recentBillPostRepository) {
		this.recentBillPostRepository = recentBillPostRepository;
	}

	public RecentBillPostsStatus readRecentBillPosts(BillPostQuery query) {
		return recentBillPostRepository.retrieveRecentBillPosts(query);
	}

	public RecentBillPost readRecentBillPostDetail(BillPostDetailQuery query) {
		RecentBillPost recentBillPost = recentBillPostRepository.retrieveRecentBillPost(query);
		if (recentBillPost == null) {
			throw RecentBillPostDomainException.notFound(query.billId());
		}
		return recentBillPost;
	}
}
