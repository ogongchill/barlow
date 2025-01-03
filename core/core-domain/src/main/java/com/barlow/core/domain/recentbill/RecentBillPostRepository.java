package com.barlow.core.domain.recentbill;

import org.springframework.stereotype.Repository;

@Repository
public interface RecentBillPostRepository {
	RecentBillPostsStatus retrieveRecentBillPosts(BillPostQuery query);
}
