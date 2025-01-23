package com.barlow.storage.db.core;

public enum PartyName {

	PEOPLE_POWER("국민의힘"),
	MINJOO("더불어민주당"),
	REBUILDING("조국혁신당"),
	NEW_REFORM("개혁신당"),
	PROGRESSIVE("진보당"),
	NEW_FUTURE("새로운미래"),
	BASIC_INCOME("기본소득당"),
	SOCIAL_DEMOCRATIC("사회민주당"),
	;

	private final String value;

	PartyName(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
