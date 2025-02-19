package com.barlow.core.enumerate;

import java.util.Arrays;

public enum ProposerType {

	GOVERNMENT("정부"),
	CHAIRMAN("위원장"),
	SPEAKER("의장"),
	LAWMAKER("의원");

	private final String value;

	public static ProposerType findByValue(String value) {
		return Arrays.stream(ProposerType.values())
			.filter(proposerType -> proposerType.value.equals(value))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException(
				String.format("기존에 존재하지 않던 ProposerType 입니다 : %s", value)
			));
	}

	ProposerType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
