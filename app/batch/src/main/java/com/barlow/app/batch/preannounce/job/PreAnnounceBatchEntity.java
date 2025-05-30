package com.barlow.app.batch.preannounce.job;

import java.time.LocalDateTime;

import com.barlow.core.enumerate.LegislationType;

public record PreAnnounceBatchEntity(
	String billId,
	String billName,
	String proposers,
	LegislationType legislationType,
	LocalDateTime deadlineDate,
	String linkUrl
) {
}
