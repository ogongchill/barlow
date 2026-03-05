package com.barlow.core.service.legislationaccount.impl;

import org.springframework.stereotype.Component;

import com.barlow.core.domain.User;
import com.barlow.core.domain.subscribe.Subscription;
import com.barlow.core.service.subscribe.SubscriptionReader;

@Component
public class LegislationAccountWithdrawalHandler {

	private final SubscriptionReader subscribeReader;
	private final LegislationAccountSubscriptionManager legislationAccountSubscriptionManager;

	public LegislationAccountWithdrawalHandler(SubscriptionReader subscribeReader,
		LegislationAccountSubscriptionManager legislationAccountSubscriptionManager) {
		this.subscribeReader = subscribeReader;
		this.legislationAccountSubscriptionManager = legislationAccountSubscriptionManager;
	}

	public void handle(User user) {
		subscribeReader.readSubscriptions(user).stream().filter(Subscription::isActive).forEach(
			subscribe -> legislationAccountSubscriptionManager.unsubscribe(subscribe.getLegislationType(), user));
	}
}
