package com.barlow.batch.core.tracebill.job;

public interface UpdatedBillShareRepository {
	void save(String key, UpdatedBills value);
	UpdatedBills findByKey(String key);
}
