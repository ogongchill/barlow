package com.barlow.batch.core.preannounce.job;

public interface PreAnnounceBillBatchRepository {
	PreviousPreAnnounceBillIds retrieveAllInProgress();
}
