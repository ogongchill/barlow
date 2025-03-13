package com.barlow.batch.core.preannounce.job;

import java.time.LocalDate;

public interface PreAnnounceBillPostBatchRepository {

	PreviousPreAnnounceBillIds retrieveAllInProgress(LocalDate today);

	void updateBillPostPreAnnounceInfo(NewPreAnnounceBills newPreAnnounceBills);
}
