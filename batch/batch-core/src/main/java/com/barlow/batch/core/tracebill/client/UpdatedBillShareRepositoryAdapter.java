package com.barlow.batch.core.tracebill.client;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.barlow.batch.core.tracebill.job.UpdatedBillShareRepository;
import com.barlow.batch.core.tracebill.job.UpdatedBills;

@Component
public class UpdatedBillShareRepositoryAdapter implements UpdatedBillShareRepository {

	private static final Map<String, UpdatedBills> STORAGE = new HashMap<>();

	@Override
	public void save(String key, UpdatedBills value) {
		STORAGE.put(key, value);
	}

	@Override
	public UpdatedBills findByKey(String key) {
		return STORAGE.get(key);
	}
}
