package com.barlow.storage.db.core;

public enum ProposerType {

	GOVERNMENT("정부"),
	CHAIRMAN("소관위원장"),
	SPEAKER("국회의장"),
	LAWMAKER("국회의원")
	;

	private final String value;

	ProposerType(String value) {
		this.value = value;
	}
}
