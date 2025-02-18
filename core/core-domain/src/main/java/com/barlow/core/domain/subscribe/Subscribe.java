package com.barlow.core.domain.subscribe;

import com.barlow.core.domain.User;

public class Subscribe {

	private final User subscriber;
	private final SubscriptionInfo subscriptionInfo;

	public Subscribe(User subscriber, long legislationAccountNo, boolean isSubscribed) {
		this.subscriber = subscriber;
		this.subscriptionInfo = new SubscriptionInfo(legislationAccountNo, isSubscribed);
	}

	Subscribe activate() {
		return new Subscribe(subscriber, subscriptionInfo.legislationAccountNo, true);
	}

	Subscribe deactivate() {
		return new Subscribe(subscriber, subscriptionInfo.legislationAccountNo, false);
	}

	public boolean isActive() {
		return subscriptionInfo.isSubscribed;
	}

	public User getSubscriber() {
		return subscriber;
	}

	public long getLegislationAccountNo() {
		return subscriptionInfo.legislationAccountNo;
	}

	static class SubscriptionInfo {

		private final long legislationAccountNo;
		private final boolean isSubscribed;

		public SubscriptionInfo(long legislationAccountNo, boolean isSubscribed) {
			this.legislationAccountNo = legislationAccountNo;
			this.isSubscribed = isSubscribed;
		}
	}
}
