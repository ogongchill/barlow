package com.barlow.app.batch.tracebill.job;

import com.barlow.core.enumerate.LegislationType;
import com.barlow.core.enumerate.ProgressStatus;

public record PreviousBillBatchEntity(
	String billId,
	String billName,
	ProgressStatus status,
	LegislationType legislationType
) {
}
