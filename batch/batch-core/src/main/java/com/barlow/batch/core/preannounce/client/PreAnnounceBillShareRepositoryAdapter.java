package com.barlow.batch.core.preannounce.client;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.barlow.batch.core.preannounce.job.NewPreAnnounceBills;
import com.barlow.batch.core.preannounce.job.PreAnnounceBillShareRepository;

@Component
public class PreAnnounceBillShareRepositoryAdapter implements PreAnnounceBillShareRepository {

	private static final Map<String, NewPreAnnounceBills> STORAGE = new HashMap<>();

	@Override
	public void save(String key, NewPreAnnounceBills newPreAnnounceBills) {
		STORAGE.put(key, newPreAnnounceBills);
	}

	@Override
	public NewPreAnnounceBills findByKey(String key) {
		return STORAGE.get(key);
	}
}
