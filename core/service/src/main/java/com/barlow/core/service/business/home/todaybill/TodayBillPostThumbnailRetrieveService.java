package com.barlow.core.service.business.home.todaybill;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.barlow.core.domain.home.todaybill.TodayBillPostRepository;
import com.barlow.core.domain.home.todaybill.TodayBillPostThumbnail;

@Service
public class TodayBillPostThumbnailRetrieveService {

	private final TodayBillPostRepository todayBillPostRepository;

	public TodayBillPostThumbnailRetrieveService(TodayBillPostRepository todayBillPostRepository) {
		this.todayBillPostRepository = todayBillPostRepository;
	}

	public List<TodayBillPostThumbnail> retrieveTodayAll(LocalDate today) {
		return todayBillPostRepository.retrieveTodayBillPostThumbnails(today);
	}
}
