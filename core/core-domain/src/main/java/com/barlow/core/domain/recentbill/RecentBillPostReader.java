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
}
