package com.barlow.storage.db.core;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Component;

import com.barlow.batch.core.tracebill.job.PreviousBillBatchEntity;
import com.barlow.batch.core.tracebill.job.PreviousBillBatchRepository;

@Component
public class PreviousBillBatchRepositoryAdapter implements PreviousBillBatchRepository {

	private final BatchTraceBillJpaRepository traceBillJpaRepository;

	public PreviousBillBatchRepositoryAdapter(BatchTraceBillJpaRepository traceBillJpaRepository) {
		this.traceBillJpaRepository = traceBillJpaRepository;
	}

	@Override
	public List<PreviousBillBatchEntity> findAllPreviousBetween(LocalDate start, LocalDate end) {
		return traceBillJpaRepository.findAllByCreatedAtBetween(start.atStartOfDay(), end.atStartOfDay())
			.stream()
			.map(BatchTraceBillJpaEntity::toPreviousBillBatchEntity)
			.toList();
	}
}
