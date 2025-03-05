package com.barlow.core.domain.home.todaybill;

import java.time.LocalDate;

public record TodayBillPostThumbnail(
	String billId,
	String billName,
	String proposers,
	LocalDate createdAt
) {
}
