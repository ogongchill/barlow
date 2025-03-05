package com.barlow.storage.db.core;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Component;

import com.barlow.core.domain.home.todaybill.TodayBillPostRepository;
import com.barlow.core.domain.home.todaybill.TodayBillPostThumbnail;

@Component
public class TodayBillPostRepositoryAdapter implements TodayBillPostRepository {

	private final BillPostJpaRepository billPostJpaRepository;

	public TodayBillPostRepositoryAdapter(BillPostJpaRepository billPostJpaRepository) {
		this.billPostJpaRepository = billPostJpaRepository;
	}

	@Override
	public List<TodayBillPostThumbnail> retrieveTodayBillPostThumbnails(LocalDate today) {
		LocalDateTime todayStart = today.atStartOfDay();
		LocalDateTime todayEnd = today.plusDays(1).atStartOfDay();
		List<BillPostJpaEntity> jpaEntities = billPostJpaRepository.findAllByCreatedAtBetween(todayStart, todayEnd);
		if (jpaEntities == null) {
			return List.of();
		}
		return jpaEntities.stream()
			.map(BillPostJpaEntity::toTodayBillPostThumbnail)
			.toList();
	}
}
