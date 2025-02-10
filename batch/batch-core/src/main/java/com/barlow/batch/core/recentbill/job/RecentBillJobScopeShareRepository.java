package com.barlow.batch.core.recentbill.job;

public interface RecentBillJobScopeShareRepository {

	void save(String key, TodayBillInfoResult value);

	TodayBillInfoResult findByKey(String key);
}
