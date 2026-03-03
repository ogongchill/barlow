package com.barlow.core.domain.billpost;

import org.springframework.stereotype.Service;

import com.barlow.core.domain.Passport;

@Service
public class BillPostRetrieveService {

	private final BillPostReader billPostReader;
	private final BillPostViewCountCache billPostViewCountCache;

	public BillPostRetrieveService(BillPostReader billPostReader, BillPostViewCountCache billPostViewCountCache) {
		this.billPostReader = billPostReader;
		this.billPostViewCountCache = billPostViewCountCache;
	}

	public BillPostsStatus readBillPosts(BillPostQuery query) {
		return billPostReader.readBillPosts(query);
	}

	public BillPost readBillPostDetail(Passport passport, BillPostDetailQuery query) {
		boolean shouldCountView = billPostViewCountCache.shouldCountView(passport, query.billId());
		return billPostReader.readBillPostDetail(query, shouldCountView);
	}
}
