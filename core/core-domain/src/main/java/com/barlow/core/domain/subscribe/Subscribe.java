package com.barlow.core.domain.subscribe;

import com.barlow.core.domain.User;

public class Subscribe {

	private final User subscriber;
	private final SubscriptionInfo subscriptionInfo;

	public Subscribe(User subscriber, String legislationAccountType, boolean isSubscribed) {
		this.subscriber = subscriber;
		this.subscriptionInfo = new SubscriptionInfo(legislationAccountType, isSubscribed);
	}

	Subscribe activate() {
		return new Subscribe(subscriber, subscriptionInfo.legislationAccountType, true);
	}

	Subscribe deactivate() {
		return new Subscribe(subscriber, subscriptionInfo.legislationAccountType, false);
	}

	public boolean isActive() {
		return subscriptionInfo.isSubscribed;
	}

	public User getSubscriber() {
		return subscriber;
	}

	public String getLegislationAccountType() {
		return subscriptionInfo.legislationAccountType;
	}

	static class SubscriptionInfo {

		private final String legislationAccountType;
		private final boolean isSubscribed;

		public SubscriptionInfo(String legislationAccountType, boolean isSubscribed) {
			this.legislationAccountType = legislationAccountType;
			this.isSubscribed = isSubscribed;
		}
	}
}
