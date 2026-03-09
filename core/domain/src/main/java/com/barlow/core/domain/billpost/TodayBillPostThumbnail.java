package com.barlow.core.domain.billpost;

import java.time.LocalDate;

public record TodayBillPostThumbnail(String billId, String billName, String proposers, LocalDate createdAt) {
}
