package com.barlow.core.domain.legislationaccount;

import org.springframework.stereotype.Service;

import com.barlow.core.domain.User;

@Service
public class LegislationAccountSubscribeService {

	private final LegislationAccountSubscriptionManager subscriptionManager;

	public LegislationAccountSubscribeService(LegislationAccountSubscriptionManager subscriptionManager) {
		this.subscriptionManager = subscriptionManager;
	}

	public void subscribeAccount(long accountNo, User user) {
		subscriptionManager.subscribe(accountNo, user);
	}

	public void unsubscribeAccount(long accountNo, User user) {
		subscriptionManager.unsubscribe(accountNo, user);
	}
}
