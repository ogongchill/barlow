package com.barlow.core.service.billpost;

import org.springframework.stereotype.Service;

import com.barlow.core.domain.Passport;
import com.barlow.core.domain.billpost.BillPost;
import com.barlow.core.domain.billpost.BillPostDetailQuery;
import com.barlow.core.domain.billpost.BillPostQuery;
import com.barlow.core.domain.billpost.BillPostsStatus;

@Service
public class BillPostRetrieveService {

	private final BillPostReader billPostReader;
	private final BillPostCacheService billPostCacheService;

	public BillPostRetrieveService(BillPostReader billPostReader, BillPostCacheService billPostCacheService) {
		this.billPostReader = billPostReader;
		this.billPostCacheService = billPostCacheService;
	}

	public BillPostsStatus readBillPosts(BillPostQuery query) {
		return billPostReader.readBillPosts(query);
	}

	public BillPost readBillPostDetail(Passport passport, BillPostDetailQuery query) {
		boolean shouldCountView = billPostCacheService.shouldCountView(passport.getUserNo(), query.billId());
		BillPost billPost = billPostCacheService.readBillPost(query);
		if (shouldCountView) {
			billPostReader.updateViewCount(query.billId());
		}
		return billPost;
	}
}
