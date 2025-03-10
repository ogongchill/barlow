package com.barlow.core.domain.subscribe;

import org.springframework.stereotype.Component;

import com.barlow.core.domain.User;
import com.barlow.core.enumerate.LegislationType;

@Component
public class SubscribeActivator {

	private final SubscribeReader subscribeReader;
	private final SubscribeRepository subscribeRepository;

	public SubscribeActivator(SubscribeReader subscribeReader, SubscribeRepository subscribeRepository) {
		this.subscribeReader = subscribeReader;
		this.subscribeRepository = subscribeRepository;
	}

	public void activate(LegislationType legislationType, User user) {
		Subscribe subscribe = subscribeReader.readSubscribe(legislationType, user);
		if (subscribe.isActive()) {
			throw SubscribeDomainException.alreadySubscribed(legislationType);
		}
		subscribeRepository.save(subscribe.activate());
	}

	public void deactivate(LegislationType legislationType, User user) {
		Subscribe subscribe = subscribeReader.readSubscribe(legislationType, user);
		if (!subscribe.isActive()) {
			throw SubscribeDomainException.alreadyUnSubscribed(legislationType);
		}
		subscribeRepository.delete(subscribe.deactivate());
	}
}
