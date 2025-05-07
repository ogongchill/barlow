package com.barlow.core.enumerate;

import java.util.Arrays;
import java.util.List;

public enum ProposerType {

	GOVERNMENT("정부"),
	CHAIRMAN("위원장"),
	SPEAKER("의장"),
	LAWMAKER("의원"),

	ETC("기타")
	;

	private final String value;

	public static ProposerType findByValue(String value) {
		return Arrays.stream(ProposerType.values())
			.filter(proposerType -> proposerType.value.equals(value))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException(
				String.format("기존에 존재하지 않던 ProposerType 입니다 : %s", value)
			));
	}

	public static List<String> findDefaultTagNames() {
		return Arrays.stream(ProposerType.values())
			.map(Enum::name)
			.toList();
	}

	public boolean isGovernment() {
		return this == GOVERNMENT;
	}

	public boolean isChairman() {
		return this == CHAIRMAN;
	}

	public boolean isSpeaker() {
		return this == SPEAKER;
	}

	public boolean isLawmaker() {
		return this == LAWMAKER;
	}

	public boolean isEtc() {
		return this == ETC;
	}

	ProposerType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
