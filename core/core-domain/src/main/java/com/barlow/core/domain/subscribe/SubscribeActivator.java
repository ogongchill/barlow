package com.barlow.core.domain.subscribe;

import org.springframework.stereotype.Component;

import com.barlow.core.domain.User;

@Component
public class SubscribeActivator {

	private final SubscribeReader subscribeReader;
	private final SubscribeRepository subscribeRepository;

	public SubscribeActivator(SubscribeReader subscribeReader, SubscribeRepository subscribeRepository) {
		this.subscribeReader = subscribeReader;
		this.subscribeRepository = subscribeRepository;
	}

	public void activate(long legislationAccountNo, User user) {
		Subscribe subscribe = subscribeReader.readSubscribe(legislationAccountNo, user);
		if (subscribe.isActive()) {
			throw SubscribeDomainException.alreadySubscribed(legislationAccountNo);
		}
		subscribeRepository.save(subscribe.activate());
	}

	public void deactivate(long legislationAccountNo, User user) {
		Subscribe subscribe = subscribeReader.readSubscribe(legislationAccountNo, user);
		if (!subscribe.isActive()) {
			throw SubscribeDomainException.alreadyUnSubscribed(legislationAccountNo);
		}
		subscribeRepository.delete(subscribe.deactivate());
	}
}
