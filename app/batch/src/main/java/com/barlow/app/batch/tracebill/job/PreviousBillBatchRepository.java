package com.barlow.app.batch.tracebill.job;

import java.time.LocalDate;
import java.util.List;

public interface PreviousBillBatchRepository {
	List<PreviousBillBatchEntity> findAllPreviousBetween(LocalDate start, LocalDate end);
}
