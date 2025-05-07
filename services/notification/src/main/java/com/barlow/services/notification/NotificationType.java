package com.barlow.services.notification;

public enum NotificationType {

	DEFAULT("기본입법알림"),
	STANDING_COMMITTEE("소관위원회알림"),
	;

	private final String value;

	NotificationType(String value) {
		this.value = value;
	}

	boolean isCommittee() {
		return this == STANDING_COMMITTEE;
	}

	public String getValue() {
		return value;
	}
}
