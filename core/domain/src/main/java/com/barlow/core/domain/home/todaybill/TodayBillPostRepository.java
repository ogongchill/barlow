package com.barlow.core.domain.home.todaybill;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface TodayBillPostRepository {
	List<TodayBillPostThumbnail> retrieveTodayBillPostThumbnails(LocalDate today);
}
