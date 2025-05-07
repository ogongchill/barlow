package com.barlow.core.storage.batch;

import org.springframework.stereotype.Component;

import com.barlow.app.batch.tracebill.job.LegislationAccountBatchRepository;
import com.barlow.app.batch.tracebill.job.UpdatedBills;

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
