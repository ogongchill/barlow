package com.barlow.core.storage.batch;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Component;

import com.barlow.app.batch.tracebill.job.PreviousBillBatchEntity;
import com.barlow.app.batch.tracebill.job.PreviousBillBatchRepository;

@Component
public class PreviousBillBatchRepositoryAdapter implements PreviousBillBatchRepository {

	private final BillPostBatchJpaRepository billPostBatchJpaRepository;

	public PreviousBillBatchRepositoryAdapter(BillPostBatchJpaRepository billPostBatchJpaRepository) {
		this.billPostBatchJpaRepository = billPostBatchJpaRepository;
	}

	@Override
	public List<PreviousBillBatchEntity> findAllPreviousBetween(LocalDate start, LocalDate end) {
		return billPostBatchJpaRepository.findAllByCreatedAtBetween(start.atStartOfDay(), end.atStartOfDay())
			.stream()
			.map(jpaEntity -> new PreviousBillBatchEntity(
				jpaEntity.getBillId(),
				jpaEntity.getBillName(),
				jpaEntity.getProgressStatus(),
				jpaEntity.getLegislationType()
			))
			.toList();
	}
}
