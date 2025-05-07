package com.barlow.app.batch.tracebill.job;

public interface BillPostBatchRepository {
	void updateAllInBatch(UpdatedBills updatedBills);
}
