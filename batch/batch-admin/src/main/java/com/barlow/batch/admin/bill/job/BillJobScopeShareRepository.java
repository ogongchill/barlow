package com.barlow.batch.admin.bill.job;

public interface BillJobScopeShareRepository {

	void save(String key, BillInfoBatchEntity value);

	BillInfoBatchEntity findByKey(String key);
}
