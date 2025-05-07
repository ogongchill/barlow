package com.barlow.app.batch.tracebill.client;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.barlow.app.batch.tracebill.job.UpdatedBillShareRepository;
import com.barlow.app.batch.tracebill.job.UpdatedBills;

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
