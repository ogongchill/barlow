package com.barlow.core.domain.billpost;

import org.springframework.stereotype.Repository;

@Repository
public interface BillPostRepository {

	BillPostsStatus retrieveRecentBillPosts(BillPostQuery query);

	BillPost retrieveRecentBillPost(BillPostDetailQuery query);
}
