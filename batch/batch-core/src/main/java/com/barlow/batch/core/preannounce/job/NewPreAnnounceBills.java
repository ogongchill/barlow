package com.barlow.batch.core.preannounce.job;

import java.util.List;

public record NewPreAnnounceBills(
	List<PreAnnounceBatchEntity> values
) {
}
