package com.barlow.core.domain.subscribe;

import com.barlow.core.enumerate.LegislationType;

public class Subscription {

	private final long subscriberNo;
	private final SubscriptionInfo info;

	public Subscription(long subscriberNo, long subscribeAccountNo, LegislationType subscribeAccountType,
		boolean isSubscribed) {
		this.subscriberNo = subscriberNo;
		this.info = new SubscriptionInfo(subscribeAccountNo, subscribeAccountType, isSubscribed);
	}

	public Subscription activate() {
		return new Subscription(subscriberNo, info.subscribeAccountNo(), info.subscribeAccountType(), true);
	}

	public Subscription deactivate() {
		return new Subscription(subscriberNo, info.subscribeAccountNo(), info.subscribeAccountType(), false);
	}

	public boolean isActive() {
		return info.isSubscribed();
	}

	public long getSubscriberNo() {
		return subscriberNo;
	}

	public long getSubscribeAccountNo() {
		return info.subscribeAccountNo();
	}

	public String getLegislationAccountType() {
		return info.subscribeAccountType().getValue();
	}

	public LegislationType getLegislationType() {
		return info.subscribeAccountType();
	}

	record SubscriptionInfo(long subscribeAccountNo, LegislationType subscribeAccountType, boolean isSubscribed) {
	}
}