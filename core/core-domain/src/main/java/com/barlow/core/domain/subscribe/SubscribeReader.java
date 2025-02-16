package com.barlow.core.domain.subscribe;

import org.springframework.stereotype.Component;

import com.barlow.core.domain.User;

@Component
public class SubscribeReader {

	private final SubscribeRepository subscribeRepository;

	public SubscribeReader(SubscribeRepository subscribeRepository) {
		this.subscribeRepository = subscribeRepository;
	}

	public Subscribe readSubscribe(long legislationAccountNo, User user) {
		SubscribeQuery query = new SubscribeQuery(legislationAccountNo, user);
		return subscribeRepository.retrieve(query);
	}
}
