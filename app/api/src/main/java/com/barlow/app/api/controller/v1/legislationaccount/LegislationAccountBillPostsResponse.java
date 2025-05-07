package com.barlow.app.api.controller.v1.legislationaccount;

import java.util.List;

public record LegislationAccountBillPostsResponse(
	List<BillPostThumbnail> today,
	List<BillPostThumbnail> recent,
	boolean isLastPage
) {
	record BillPostThumbnail(
		String billId,
		String billName,
		String proposers,
		String legislationProcess
	) {
	}
}
