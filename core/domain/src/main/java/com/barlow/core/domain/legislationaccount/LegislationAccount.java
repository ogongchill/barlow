package com.barlow.core.domain.legislationaccount;

import com.barlow.core.enumerate.LegislationType;

public class LegislationAccount {

	private final long no;
	private final LegislationType type;
	private final String description;
	private final int postCount;
	private final int subscriberCount;
	private final boolean isSubscribed;
	private final boolean isNotifiable;

	public LegislationAccount(
		long no,
		LegislationType type,
		String description,
		int postCount,
		int subscriberCount
	) {
		this(no, type, description, postCount, subscriberCount, false, false);
	}

	private LegislationAccount(
		long no,
		LegislationType type,
		String description,
		int postCount,
		int subscriberCount,
		boolean isSubscribed,
		boolean isNotifiable
	) {
		this.no = no;
		this.type = type;
		this.description = description;
		this.postCount = postCount;
		this.subscriberCount = subscriberCount;
		this.isSubscribed = isSubscribed;
		this.isNotifiable = isNotifiable;
	}

	public LegislationAccount withSubscribed(boolean subscribed) {
		return new LegislationAccount(no, type, description, postCount, subscriberCount, subscribed, isNotifiable);
	}

	public LegislationAccount withNotifiable(boolean notifiable) {
		return new LegislationAccount(no, type, description, postCount, subscriberCount, isSubscribed, notifiable);
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
