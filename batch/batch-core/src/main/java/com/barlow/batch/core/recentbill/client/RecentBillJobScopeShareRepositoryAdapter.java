package com.barlow.batch.core.recentbill.client;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.barlow.batch.core.recentbill.job.RecentBillJobScopeShareRepository;
import com.barlow.batch.core.recentbill.job.TodayBillInfoResult;

@Component
public class RecentBillJobScopeShareRepositoryAdapter implements RecentBillJobScopeShareRepository {

	private static final Map<String, TodayBillInfoResult> STORAGE = new HashMap<>();

	@Override
	public void save(String key, TodayBillInfoResult value) {
		STORAGE.put(key, value);
	}

	@Override
	public TodayBillInfoResult findByKey(String key) {
		return STORAGE.get(key);
	}
}
