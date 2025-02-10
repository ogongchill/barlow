package com.barlow.storage.db.core;

import java.util.Arrays;

public enum PartyName {

	PEOPLE_POWER("국민의힘"),
	MINJOO("더불어민주당"),
	REBUILDING("조국혁신당"),
	NEW_REFORM("개혁신당"),
	PROGRESSIVE("진보당"),
	NEW_FUTURE("새로운미래"),
	BASIC_INCOME("기본소득당"),
	SOCIAL_DEMOCRATIC("사회민주당"),
	INDEPENDENT("무소속")
	;

	private final String value;

	PartyName(String value) {
		this.value = value;
	}

	public static PartyName findByValue(String value) {
		return Arrays.stream(PartyName.values())
			.filter(proposerType -> proposerType.value.equals(value))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException(
				String.format("기존에 존재하지 않던 PartyName 입니다 : %s", value)
			));
	}

	public String getValue() {
		return value;
	}
}
