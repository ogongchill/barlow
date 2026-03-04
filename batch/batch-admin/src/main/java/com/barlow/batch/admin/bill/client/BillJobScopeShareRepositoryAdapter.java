package com.barlow.batch.admin.bill.client;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.barlow.batch.admin.bill.job.BillInfoBatchEntity;
import com.barlow.batch.admin.bill.job.BillJobScopeShareRepository;

@Component
public class BillJobScopeShareRepositoryAdapter implements BillJobScopeShareRepository {

	private static final Map<String, BillInfoBatchEntity> STORAGE = new HashMap<>();

	@Override
	public void save(String key, BillInfoBatchEntity value) {
		STORAGE.put(key, value);
	}

	@Override
	public BillInfoBatchEntity findByKey(String key) {
		return STORAGE.get(key);
	}
}
