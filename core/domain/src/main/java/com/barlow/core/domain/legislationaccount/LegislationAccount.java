package com.barlow.core.domain.legislationaccount;

import com.barlow.core.enumerate.LegislationType;

public class LegislationAccount {

	private final long no;
	private final LegislationType type;
	private final String description;
	private final int postCount;
	private final int subscriberCount;
	private boolean isSubscribed;
	private boolean isNotifiable;

	public LegislationAccount(
		long no,
		LegislationType type,
		String description,
		int postCount,
		int subscriberCount
	) {
		this.no = no;
		this.type = type;
		this.description = description;
		this.postCount = postCount;
		this.subscriberCount = subscriberCount;
	}

	void setSubscribed(boolean subscribed) {
		isSubscribed = subscribed;
	}

	void setNotifiable(boolean notifiable) {
		isNotifiable = notifiable;
	}

	public long getNo() {
		return no;
	}

	public LegislationType getType() {
		return type;
	}

	public String getLegislationType() {
		return type.getValue();
	}

	public String getIconPath() {
		return type.getIconPath();
	}

	public String getDescription() {
		return description;
	}

	public int getPostCount() {
		return postCount;
	}

	public int getSubscriberCount() {
		return subscriberCount;
	}

	public boolean isSubscribed() {
		return isSubscribed;
	}

	public boolean isNotifiable() {
		return isNotifiable;
	}
}
