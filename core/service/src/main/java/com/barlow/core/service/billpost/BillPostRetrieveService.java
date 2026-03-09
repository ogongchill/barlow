package com.barlow.core.service.billpost;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.barlow.core.domain.Passport;
import com.barlow.core.domain.billpost.BillPost;
import com.barlow.core.domain.billpost.BillPostDetailQuery;
import com.barlow.core.domain.billpost.BillPostQuery;
import com.barlow.core.domain.billpost.BillPostsStatus;

@Service
public class BillPostRetrieveService {

	private final BillPostReader billPostReader;
	private final BillPostCacheService billPostCacheService;
	private final BillPostViewCountUpdater billPostViewCountUpdater;

	public BillPostRetrieveService(BillPostReader billPostReader, BillPostCacheService billPostCacheService,
		BillPostViewCountUpdater billPostViewCountUpdater) {
		this.billPostReader = billPostReader;
		this.billPostCacheService = billPostCacheService;
		this.billPostViewCountUpdater = billPostViewCountUpdater;
	}

	public BillPostsStatus readBillPosts(BillPostQuery query) {
		return billPostReader.readBillPosts(query);
	}

	@Transactional
	public BillPost readBillPostDetail(Passport passport, BillPostDetailQuery query) {
		boolean shouldCountView = billPostCacheService.shouldCountView(passport.getUserNo(), query.billId());
		BillPost billPost = billPostCacheService.readBillPost(query);
		if (shouldCountView) {
			billPostViewCountUpdater.update(query.billId());
		}
		return billPost;
	}
}
