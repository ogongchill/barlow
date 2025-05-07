package com.barlow.app.batch.preannounce.job;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record NewPreAnnounceBills(
	List<PreAnnounceBatchEntity> values
) {
	public Map<String, PreAnnounceBatchEntity> getBillIdWithPreAnnounceBill() {
		return values.stream()
			.collect(Collectors.toMap(
				PreAnnounceBatchEntity::billId,
				value -> value
			));
	}
}
