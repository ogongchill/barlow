package com.barlow.client.knal.api.code;

import java.util.Arrays;
import java.util.NoSuchElementException;

public enum CommitteeResultCode {

	D01("가결"),
	D02("부결"),
	D03("폐기"),
	D04("계류");

	private final String description;

	CommitteeResultCode(String description) {
		this.description = description;
	}

	public static CommitteeResultCode find(String target) {
		return Arrays.stream(CommitteeResultCode.values())
			.filter(code -> code.description.equals(target))
			.findFirst()
			.orElseThrow(() -> new NoSuchElementException("no such result name:" + target));
	}
}
