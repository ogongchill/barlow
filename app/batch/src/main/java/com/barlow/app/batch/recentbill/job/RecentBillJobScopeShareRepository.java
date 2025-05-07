package com.barlow.app.batch.recentbill.job;

public interface RecentBillJobScopeShareRepository {

	void save(String key, TodayBillInfoBatchEntity value);

	TodayBillInfoBatchEntity findByKey(String key);
}
