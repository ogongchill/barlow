package com.barlow.storage.db.core;

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
	public PreviousPreAnnounceBillIds retrieveAllInProgress() {
		return new PreviousPreAnnounceBillIds(
			jpaRepository.findAllInProgress(PreAnnounceBillJpaEntity.ProgressStatus.IN_PROGRESS.name())
				.stream()
				.map(PreAnnounceBillJpaEntity::getBillId)
				.toList()
		);
	}
}
