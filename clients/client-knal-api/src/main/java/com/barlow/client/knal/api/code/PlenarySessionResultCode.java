package com.barlow.client.knal.api.code;

import java.util.Arrays;
import java.util.NoSuchElementException;

/**
 * 본회의처리결과 코드
 */
public enum PlenarySessionResultCode {

	E01("가결"),
	E02("부결"),
	E03("대안반영폐기"),
	E04("폐기"),
	E05("임기만료폐기"),
	E06("철회"),
	E07("계류");

	private final String description;

	PlenarySessionResultCode(String description) {
		this.description = description;
	}

	public static PlenarySessionResultCode find(String target) {
		return Arrays.stream(PlenarySessionResultCode.values())
			.filter(code -> code.description.equals(target))
			.findFirst()
			.orElseThrow(() -> new NoSuchElementException("no such plenarySessionResultCode : " + target));
	}
}
