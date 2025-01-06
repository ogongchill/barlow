package com.barlow.storage.db.core;

public enum PartyName {

	; // 정당명

	private final String value;

	PartyName(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
