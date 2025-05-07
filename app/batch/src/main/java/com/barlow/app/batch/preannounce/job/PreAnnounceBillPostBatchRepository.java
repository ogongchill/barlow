package com.barlow.app.batch.preannounce.job;

import java.time.LocalDate;

public interface PreAnnounceBillPostBatchRepository {

	PreviousPreAnnounceBillIds retrieveAllInProgress(LocalDate today);

	void updateBillPostPreAnnounceInfo(NewPreAnnounceBills newPreAnnounceBills);
}
