package com.barlow.core.domain.legislationaccount;

import org.springframework.stereotype.Service;

import com.barlow.core.domain.User;
import com.barlow.core.enumerate.LegislationType;

@Service
public class LegislationAccountSubscribeService {

	private final LegislationAccountSubscriptionManager subscriptionManager;

	public LegislationAccountSubscribeService(LegislationAccountSubscriptionManager subscriptionManager) {
		this.subscriptionManager = subscriptionManager;
	}

	public void subscribeAccount(LegislationType legislationType, User user) {
		subscriptionManager.subscribe(legislationType, user);
	}

	public void unsubscribeAccount(LegislationType legislationType, User user) {
		subscriptionManager.unsubscribe(legislationType, user);
	}
}
