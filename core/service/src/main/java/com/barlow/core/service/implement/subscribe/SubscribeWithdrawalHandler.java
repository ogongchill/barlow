package com.barlow.core.service.implement.subscribe;

import org.springframework.stereotype.Component;

import com.barlow.core.domain.User;
import com.barlow.core.domain.subscribe.SubscribeRepository;

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
