package com.barlow.core.api.controller.v1.recentbill;

import java.util.List;

public record RecentBillPostsResponse(
	List<RecentBillPostThumbnail> today,
	List<RecentBillPostThumbnail> recent,
	boolean isLastPage
) {
	record RecentBillPostThumbnail(
		String billName,
		String proposers,
		String legislationProcess,
		int viewCount
	) {
	}
}
