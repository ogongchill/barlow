package com.barlow.core.domain.recentbill;

import org.springframework.stereotype.Service;

@Service
public class RecentBillPostRetrieveService {

	private final RecentBillPostReader recentBillPostReader;

	public RecentBillPostRetrieveService(RecentBillPostReader recentBillPostReader) {
		this.recentBillPostReader = recentBillPostReader;
	}

	public RecentBillPostsStatus readRecentBillPosts(BillPostQuery query) {
		return recentBillPostReader.readRecentBillPosts(query);
	}
}
