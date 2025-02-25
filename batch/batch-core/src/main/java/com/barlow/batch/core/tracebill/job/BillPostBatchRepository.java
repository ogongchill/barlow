package com.barlow.batch.core.tracebill.job;

public interface BillPostBatchRepository {
	void updateAllInBatch(UpdatedBills updatedBills);
}
