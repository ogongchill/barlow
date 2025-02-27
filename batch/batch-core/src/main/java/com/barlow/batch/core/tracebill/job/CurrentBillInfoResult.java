package com.barlow.batch.core.tracebill.job;

import java.util.Map;

import com.barlow.core.enumerate.ProgressStatus;

public record CurrentBillInfoResult(
	Map<String, ProgressStatus> result
) {
	ProgressStatus getStatusByBillId(String billId) {
		return result.get(billId);
	}
}
