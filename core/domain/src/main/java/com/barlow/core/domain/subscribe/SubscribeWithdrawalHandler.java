package com.barlow.core.domain.subscribe;

import org.springframework.stereotype.Component;

import com.barlow.core.domain.User;

@Component
public class SubscribeWithdrawalHandler {

	private final SubscribeReader subscribeReader;
	private final SubscribeRepository subscribeRepository;

	public SubscribeWithdrawalHandler(SubscribeReader subscribeReader, SubscribeRepository subscribeRepository) {
		this.subscribeReader = subscribeReader;
		this.subscribeRepository = subscribeRepository;
	}

	public void handle(User user) {
		subscribeReader.readSubscribes(user)
			.forEach(subscribeRepository::delete);
	}
}
