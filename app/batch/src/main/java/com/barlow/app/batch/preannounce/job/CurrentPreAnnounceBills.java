package com.barlow.app.batch.preannounce.job;

import java.util.List;

public class CurrentPreAnnounceBills {

	private final List<PreAnnounceBatchEntity> values;

	public CurrentPreAnnounceBills(List<PreAnnounceBatchEntity> values) {
		this.values = values;
	}

	public NewPreAnnounceBills dirtyCheck(PreviousPreAnnounceBillIds previousBills) {
		return new NewPreAnnounceBills(
			values.stream()
				.filter(currentBill -> previousBills.doesNotContainSameBillId(currentBill.billId()))
				.toList()
		);
	}
}
