package com.barlow.core.domain.billpost;

import java.util.List;

public record BillPostsStatus(
	List<BillPost> billPosts,
	boolean isLastPage
) {
}
