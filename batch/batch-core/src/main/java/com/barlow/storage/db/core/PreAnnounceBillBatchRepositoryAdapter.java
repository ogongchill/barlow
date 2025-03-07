package com.barlow.storage.db.core;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import com.barlow.batch.core.preannounce.job.PreAnnounceBillBatchRepository;
import com.barlow.batch.core.preannounce.job.PreviousPreAnnounceBillIds;

@Component
public class PreAnnounceBillBatchRepositoryAdapter implements PreAnnounceBillBatchRepository {

	private final PreAnnounceBillBatchJpaRepository jpaRepository;

	public PreAnnounceBillBatchRepositoryAdapter(PreAnnounceBillBatchJpaRepository jpaRepository) {
		this.jpaRepository = jpaRepository;
	}

	@Override
	public PreviousPreAnnounceBillIds retrieveAllInProgress(LocalDate today) {
		return new PreviousPreAnnounceBillIds(
			jpaRepository.findAllByDeadlineDateGreaterThanEqual(today.atStartOfDay())
				.stream()
				.map(PreAnnounceBillJpaEntity::getBillId)
				.toList()
		);
	}
}
