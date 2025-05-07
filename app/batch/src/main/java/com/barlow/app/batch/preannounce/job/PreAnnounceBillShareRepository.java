package com.barlow.app.batch.preannounce.job;

public interface PreAnnounceBillShareRepository {

	void save(String key, CurrentPreAnnounceBills newPreAnnounceBills);

	CurrentPreAnnounceBills findByKey(String key);
}
