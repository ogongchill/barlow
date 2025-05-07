package com.barlow.core.domain.billpost;

import org.springframework.stereotype.Service;

@Service
public class BillPostRetrieveService {

	private final BillPostReader billPostReader;

	public BillPostRetrieveService(BillPostReader billPostReader) {
		this.billPostReader = billPostReader;
	}

	public BillPostsStatus readBillPosts(BillPostQuery query) {
		return billPostReader.readBillPosts(query);
	}

	public BillPost readBillPostDetail(BillPostDetailQuery query) {
		return billPostReader.readBillPostDetail(query);
	}
}
