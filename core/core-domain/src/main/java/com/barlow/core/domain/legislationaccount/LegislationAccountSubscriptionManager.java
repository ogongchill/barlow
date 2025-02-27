package com.barlow.core.domain.legislationaccount;

import org.springframework.stereotype.Component;

import com.barlow.core.domain.User;
import com.barlow.core.domain.subscribe.SubscribeActivator;

/**
 * TODO : tx 관리 어떻게 할 것인지 고민해야함
 */
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

	public void subscribe(long accountNo, User user) {
		legislationAccountRepository.incrementSubscriber(accountNo);
		subscribeActivator.activate(accountNo, user);
	}

	public void unsubscribe(long accountNo, User user) {
		legislationAccountRepository.decrementSubscriber(accountNo);
		subscribeActivator.deactivate(accountNo, user);
	}
}
