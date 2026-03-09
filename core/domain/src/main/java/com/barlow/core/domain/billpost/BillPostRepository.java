package com.barlow.core.domain.billpost;

public interface BillPostRepository {

	BillPostsStatus retrieveRecentBillPosts(BillPostQuery query);

	BillPost retrieveRecentBillPost(BillPostDetailQuery query);

	void updateViewCount(String billId);
}
