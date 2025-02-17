package com.barlow.core.domain.recentbill;

import org.springframework.stereotype.Repository;

@Repository
public interface BillPostRepository {

	BillPostsStatus retrieveRecentBillPosts(BillPostQuery query);

	BillPost retrieveRecentBillPost(BillPostDetailQuery query);
}
