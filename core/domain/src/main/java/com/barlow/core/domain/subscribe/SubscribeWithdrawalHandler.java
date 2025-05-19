package com.barlow.core.domain.subscribe;

import org.springframework.stereotype.Component;

import com.barlow.core.domain.User;

@Component
public class SubscribeWithdrawalHandler {

	private final SubscribeRepository subscribeRepository;

	public SubscribeWithdrawalHandler(SubscribeRepository subscribeRepository) {
		this.subscribeRepository = subscribeRepository;
	}

	public void handle(User user) {
		subscribeRepository.deleteAll(user);
	}
}
