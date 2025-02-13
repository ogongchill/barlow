package com.barlow.storage.db.core;

public enum ProgressStatus {

	RECEIVED("접수"),
	COMMITTEE_RECEIVED("소관위접수"),
	COMMITTEE_REVIEW("소관위심사"),
	PLENARY_SUBMITTED("본회의부의"),
	PLENARY_DECIDED("본회의의결"),
	WITHDRAWN("철회"),
	GOVERNMENT_TRANSFERRED("정부이송"),
	REDEMAND_REQUESTED("재의요구"),
	REJECTED("재의(부결)"),
	PROMULGATED("공포"),
	;

	private final String value;

	ProgressStatus(String value) {
		this.value = value;
	}

	String getValue() {
		return value;
	}
}
