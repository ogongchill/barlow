package com.barlow.batch.core.preannounce.job;

public interface PreAnnounceBillShareRepository {

	void save(String key, NewPreAnnounceBills newPreAnnounceBills);

	NewPreAnnounceBills findByKey(String key);
}
