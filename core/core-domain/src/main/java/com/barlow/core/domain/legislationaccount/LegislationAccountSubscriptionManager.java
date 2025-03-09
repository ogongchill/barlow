package com.barlow.core.domain.legislationaccount;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.barlow.core.domain.User;
import com.barlow.core.domain.subscribe.SubscribeActivator;
import com.barlow.core.enumerate.LegislationType;

@Component
public class LegislationAccountSubscriptionManager {

	private final LegislationAccountRepository legislationAccountRepository;
	private final SubscribeActivator subscribeActivator;

	public LegislationAccountSubscriptionManager(
		LegislationAccountRepository legislationAccountRepository,
		SubscribeActivator subscribeActivator
	) {
		this.legislationAccountRepository = legislationAccountRepository;
		this.subscribeActivator = subscribeActivator;
	}

	@Transactional
	public void subscribe(LegislationType legislationType, User user) {
		subscribeActivator.activate(legislationType, user);
		legislationAccountRepository.incrementSubscriber(legislationType);
	}

	@Transactional
	public void unsubscribe(LegislationType legislationType, User user) {
		subscribeActivator.deactivate(legislationType, user);
		legislationAccountRepository.decrementSubscriber(legislationType);
	}
}
