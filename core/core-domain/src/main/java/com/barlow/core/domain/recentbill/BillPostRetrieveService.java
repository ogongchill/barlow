package com.barlow.core.domain.recentbill;

import org.springframework.stereotype.Service;

@Service
public class BillPostRetrieveService {

	private final BillPostReader billPostReader;

	public BillPostRetrieveService(BillPostReader billPostReader) {
		this.billPostReader = billPostReader;
	}

	public BillPostsStatus readRecentBillPosts(BillPostQuery query) {
		return billPostReader.readBillPosts(query);
	}

	public BillPost readRecentBillPostDetail(BillPostDetailQuery query) {
		return billPostReader.readBillPostDetail(query);
	}
}
