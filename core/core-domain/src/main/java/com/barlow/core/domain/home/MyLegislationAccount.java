package com.barlow.core.domain.home;

public class MyLegislationAccount {

	private final long no;
	private final String bodyType;
	private final String iconImagePath;

	public MyLegislationAccount(long no, String bodyType, String iconImagePath) {
		this.no = no;
		this.bodyType = bodyType;
		this.iconImagePath = iconImagePath;
	}

	public long getNo() {
		return no;
	}

	public String getBodyType() {
		return bodyType;
	}

	public String getIconImagePath() {
		return iconImagePath;
	}
}
