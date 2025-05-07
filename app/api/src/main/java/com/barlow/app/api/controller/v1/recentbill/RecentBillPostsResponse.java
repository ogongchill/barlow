package com.barlow.app.api.controller.v1.recentbill;

import java.util.List;

public record RecentBillPostsResponse(
	List<RecentBillPostThumbnail> today,
	List<RecentBillPostThumbnail> recent,
	boolean isLastPage
) {
	record RecentBillPostThumbnail(
		String billId,
		String billName,
		String proposers,
		String legislationProcess
	) {
	}
}
