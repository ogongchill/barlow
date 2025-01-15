package com.barlow.core.domain.home;

public class MyLegislationAccount {

	private final long no;
	private final String bodyType;
	private final String iconImagePath;
	private final String description;
	private final int postCount;
	private final int subscriberCount;

	public MyLegislationAccount(
		long no, String bodyType, String iconImagePath,
		String description, int postCount, int subscriberCount
	) {
		this.no = no;
		this.bodyType = bodyType;
		this.iconImagePath = iconImagePath;
		this.description = description;
		this.postCount = postCount;
		this.subscriberCount = subscriberCount;
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
