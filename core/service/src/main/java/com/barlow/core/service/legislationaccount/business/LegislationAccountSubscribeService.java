package com.barlow.core.service.legislationaccount.business;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.barlow.core.domain.User;
import com.barlow.core.enumerate.LegislationType;
import com.barlow.core.service.legislationaccount.impl.LegislationAccountSubscriptionManager;

@Service
public class LegislationAccountSubscribeService {

	private final LegislationAccountSubscriptionManager subscriptionManager;

	public LegislationAccountSubscribeService(LegislationAccountSubscriptionManager subscriptionManager) {
		this.subscriptionManager = subscriptionManager;
	}

	@Transactional
	public void subscribeAccount(LegislationType legislationType, User user) {
		subscriptionManager.subscribe(legislationType, user);
	}

	@Transactional
	public void unsubscribeAccount(LegislationType legislationType, User user) {
		subscriptionManager.unsubscribe(legislationType, user);
	}
}
