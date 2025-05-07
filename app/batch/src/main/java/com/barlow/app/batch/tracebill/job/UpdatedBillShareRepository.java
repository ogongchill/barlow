package com.barlow.app.batch.tracebill.job;

public interface UpdatedBillShareRepository {

	void save(String key, UpdatedBills value);

	UpdatedBills findByKey(String key);
}
