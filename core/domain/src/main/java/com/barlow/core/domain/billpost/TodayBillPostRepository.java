package com.barlow.core.domain.billpost;

import java.time.LocalDate;
import java.util.List;


public interface TodayBillPostRepository {
	List<TodayBillPostThumbnail> retrieveTodayBillPostThumbnails(LocalDate today);
}
