package com.barlow.core.domain.subscribe;

import com.barlow.core.domain.User;
import com.barlow.core.enumerate.LegislationType;

public class Subscribe {

	private final User subscriber;
	private final SubscriptionInfo info;

	public Subscribe(
		User subscriber,
		long subscribeAccountNo,
		LegislationType subscribeAccountType,
		boolean isSubscribed
	) {
		this.subscriber = subscriber;
		this.info = new SubscriptionInfo(subscribeAccountNo, subscribeAccountType, isSubscribed);
	}

	Subscribe activate() {
		return new Subscribe(subscriber, info.subscribeAccountNo, info.subscribeAccountType, true);
	}

	Subscribe deactivate() {
		return new Subscribe(subscriber, info.subscribeAccountNo, info.subscribeAccountType, false);
	}

	public boolean isActive() {
		return info.isSubscribed;
	}

	public User getSubscriber() {
		return subscriber;
	}

	public long getSubscribeAccountNo() {
		return info.subscribeAccountNo;
	}

	public String getLegislationAccountType() {
		return info.subscribeAccountType.getValue();
	}

	public LegislationType getLegislationType() {
		return info.subscribeAccountType;
	}

	static class SubscriptionInfo {

		private final long subscribeAccountNo;
		private final LegislationType subscribeAccountType;
		private final boolean isSubscribed;

		public SubscriptionInfo(long subscribeAccountNo, LegislationType subscribeAccountType, boolean isSubscribed) {
			this.subscribeAccountNo = subscribeAccountNo;
			this.subscribeAccountType = subscribeAccountType;
			this.isSubscribed = isSubscribed;
		}
	}
}
