package com.barlow.core.storage.batch;

import org.springframework.stereotype.Component;

import com.barlow.app.batch.tracebill.job.BillPostBatchRepository;
import com.barlow.app.batch.tracebill.job.UpdatedBills;

@Component
public class BillPostBatchRepositoryAdapter implements BillPostBatchRepository {

	private final BillPostBatchJpaRepository billPostBatchJpaRepository;

	public BillPostBatchRepositoryAdapter(BillPostBatchJpaRepository billPostBatchJpaRepository) {
		this.billPostBatchJpaRepository = billPostBatchJpaRepository;
	}

	@Override
	public void updateAllInBatch(UpdatedBills updatedBills) {
		UpdatedBills committeeReceived = updatedBills.filterCommitteeReceived();
		if (!committeeReceived.isEmpty()) {
			committeeReceived.groupByCommittee()
				.forEach(billPostBatchJpaRepository::updateLegislationTypeInBatch);
		}

		UpdatedBills nonCommitteeReceived = updatedBills.filterNonCommitteeReceived();
		if (!nonCommitteeReceived.isEmpty()) {
			nonCommitteeReceived.groupByProgressStatus()
				.forEach(billPostBatchJpaRepository::updateProgressStatusInBatch);
		}
	}
}
