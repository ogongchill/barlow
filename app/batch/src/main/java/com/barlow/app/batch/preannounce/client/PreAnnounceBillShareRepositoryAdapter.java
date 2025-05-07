package com.barlow.app.batch.preannounce.client;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.barlow.app.batch.preannounce.job.CurrentPreAnnounceBills;
import com.barlow.app.batch.preannounce.job.PreAnnounceBillShareRepository;

@Component
public class PreAnnounceBillShareRepositoryAdapter implements PreAnnounceBillShareRepository {

	private static final Map<String, CurrentPreAnnounceBills> STORAGE = new HashMap<>();

	@Override
	public void save(String key, CurrentPreAnnounceBills newPreAnnounceBills) {
		STORAGE.put(key, newPreAnnounceBills);
	}

	@Override
	public CurrentPreAnnounceBills findByKey(String key) {
		return STORAGE.get(key);
	}
}
