package com.barlow.core.domain.legislationaccount;

import org.springframework.stereotype.Component;

import com.barlow.core.domain.User;
import com.barlow.core.domain.subscribe.Subscribe;
import com.barlow.core.domain.subscribe.SubscribeReader;

@Component
public class LegislationAccountWithdrawalHandler {

	private final SubscribeReader subscribeReader;
	private final LegislationAccountSubscriptionManager legislationAccountSubscriptionManager;

	public LegislationAccountWithdrawalHandler(
		SubscribeReader subscribeReader,
		LegislationAccountSubscriptionManager legislationAccountSubscriptionManager
	) {
		this.subscribeReader = subscribeReader;
		this.legislationAccountSubscriptionManager = legislationAccountSubscriptionManager;
	}

	public void handle(User user) {
		subscribeReader.readSubscribes(user)
			.stream()
			.filter(Subscribe::isActive)
			.forEach(subscribe ->
				legislationAccountSubscriptionManager.unsubscribe(subscribe.getLegislationType(), user)
			);
	}
}
