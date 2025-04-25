package com.barlow.batch.core.preannounce.job;

import java.util.List;

public class PreviousPreAnnounceBillIds {

	private final List<String> values;

	public PreviousPreAnnounceBillIds(List<String> values) {
		this.values = values;
	}

	public boolean doesNotContainSameBillId(String billId) {
		return !values.contains(billId);
	}
}
