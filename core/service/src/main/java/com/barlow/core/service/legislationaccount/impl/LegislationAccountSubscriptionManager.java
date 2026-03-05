package com.barlow.core.service.legislationaccount.impl;

import org.springframework.stereotype.Component;

import com.barlow.core.domain.User;
import com.barlow.core.domain.legislationaccount.LegislationAccountRepository;
import com.barlow.core.enumerate.LegislationType;
import com.barlow.core.service.subscribe.SubscriptionActivator;

@Component
public class LegislationAccountSubscriptionManager {

	private final LegislationAccountRepository legislationAccountRepository;
	private final SubscriptionActivator subscribeActivator;

	public LegislationAccountSubscriptionManager(LegislationAccountRepository legislationAccountRepository,
		SubscriptionActivator subscribeActivator) {
		this.legislationAccountRepository = legislationAccountRepository;
		this.subscribeActivator = subscribeActivator;
	}

	public void subscribe(LegislationType legislationType, User user) {
		subscribeActivator.activate(legislationType, user);
		legislationAccountRepository.incrementSubscriber(legislationType);
	}

	public void unsubscribe(LegislationType legislationType, User user) {
		subscribeActivator.deactivate(legislationType, user);
		legislationAccountRepository.decrementSubscriber(legislationType);
	}
}
