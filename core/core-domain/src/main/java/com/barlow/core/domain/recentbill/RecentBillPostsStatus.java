package com.barlow.core.domain.recentbill;

import java.util.List;

public record RecentBillPostsStatus(
	List<RecentBillPost> recentBillPosts,
	boolean isLastPage
) {
}
