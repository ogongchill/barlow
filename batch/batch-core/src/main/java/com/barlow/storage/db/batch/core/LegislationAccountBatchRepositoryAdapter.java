package com.barlow.storage.db.batch.core;

import org.springframework.stereotype.Component;

import com.barlow.batch.core.tracebill.job.LegislationAccountBatchRepository;
import com.barlow.batch.core.tracebill.job.UpdatedBills;

@Component
public class LegislationAccountBatchRepositoryAdapter implements LegislationAccountBatchRepository {

	private final LegislationAccountBatchJpaRepository batchJpaRepository;

	public LegislationAccountBatchRepositoryAdapter(LegislationAccountBatchJpaRepository batchJpaRepository) {
		this.batchJpaRepository = batchJpaRepository;
	}

	@Override
	public void updateAccountBillCount(UpdatedBills committeeReceivedBills) {
		committeeReceivedBills.groupByCommittee()
			.forEach((legislationType, billIds) ->
				batchJpaRepository.updateAccountPostCount(billIds.size(), legislationType.getLegislationNo())
			);
	}
}
