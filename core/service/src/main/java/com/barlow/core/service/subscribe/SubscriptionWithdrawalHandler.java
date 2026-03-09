package com.barlow.core.service.subscribe;

import org.springframework.stereotype.Component;

import com.barlow.core.domain.User;
import com.barlow.core.domain.subscribe.SubscriptionRepository;

@Component
public class SubscriptionWithdrawalHandler {

	private final SubscriptionRepository subscribeRepository;

	public SubscriptionWithdrawalHandler(SubscriptionRepository subscribeRepository) {
		this.subscribeRepository = subscribeRepository;
	}

	public void handle(User user) {
		subscribeRepository.deleteAll(user);
	}
}
