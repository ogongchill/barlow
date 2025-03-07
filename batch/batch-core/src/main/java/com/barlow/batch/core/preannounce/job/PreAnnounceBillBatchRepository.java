package com.barlow.batch.core.preannounce.job;

import java.time.LocalDate;

public interface PreAnnounceBillBatchRepository {
	PreviousPreAnnounceBillIds retrieveAllInProgress(LocalDate today);
}
