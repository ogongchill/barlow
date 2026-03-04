package com.barlow.core.service.business.billpost;

import org.springframework.stereotype.Service;

import com.barlow.core.domain.Passport;
import com.barlow.core.domain.billpost.BillPost;
import com.barlow.core.domain.billpost.BillPostDetailQuery;
import com.barlow.core.domain.billpost.BillPostQuery;
import com.barlow.core.domain.billpost.BillPostViewCountCache;
import com.barlow.core.domain.billpost.BillPostsStatus;
import com.barlow.core.service.implement.billpost.BillPostReader;

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
