package com.barlow.storage.db.core;

public enum LegislationStatus {

	// 접수,소관위접수,본회의부의,철회,정부이송,재의요구,부결,공포 등등
	;

	private final String value;

	LegislationStatus(String value) {
		this.value = value;
	}

	String getValue() {
		return value;
	}
}
