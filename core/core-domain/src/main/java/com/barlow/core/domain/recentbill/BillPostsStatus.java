package com.barlow.core.domain.recentbill;

import java.util.List;

public record BillPostsStatus(
	List<BillPost> billPosts,
	boolean isLastPage
) {
}
