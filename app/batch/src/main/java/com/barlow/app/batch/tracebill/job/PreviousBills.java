package com.barlow.app.batch.tracebill.job;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import com.barlow.core.enumerate.ProgressStatus;

public class PreviousBills {

	private final List<PreviousBillBatchEntity> values;

	public PreviousBills(List<PreviousBillBatchEntity> values) {
		this.values = values;
	}

	public UpdatedBills dirtyCheck(CurrentBillInfoResult current) {
		Map<ProgressStatus, List<UpdatedBills.BillInfo>> map = new EnumMap<>(ProgressStatus.class);
		values.forEach(previous -> {
			ProgressStatus currentStatus = current.getStatusByBillId(previous.billId());
			if (currentStatus == null || currentStatus.equals(previous.status())) {
				return;
			}
			map.computeIfAbsent(currentStatus, status -> new ArrayList<>())
				.add(new UpdatedBills.BillInfo(previous.billId(), previous.billName(), previous.legislationType()));
		});
		return new UpdatedBills(map);
	}
}
