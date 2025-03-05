package com.barlow.core.domain.legislationaccount;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.barlow.core.domain.User;
import com.barlow.core.domain.subscribe.SubscribeActivator;

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
	public void subscribe(long accountNo, User user) {
		subscribeActivator.activate(accountNo, user);
		legislationAccountRepository.incrementSubscriber(accountNo);
	}

	@Transactional
	public void unsubscribe(long accountNo, User user) {
		subscribeActivator.deactivate(accountNo, user);
		legislationAccountRepository.decrementSubscriber(accountNo);
	}
}
