package com.barlow.core.service.subscribe;

import org.springframework.stereotype.Component;

import com.barlow.core.domain.User;
import com.barlow.core.domain.subscribe.Subscription;
import com.barlow.core.domain.subscribe.SubscriptionDomainException;
import com.barlow.core.domain.subscribe.SubscriptionRepository;
import com.barlow.core.enumerate.LegislationType;

@Component
public class SubscriptionActivator {

	private final SubscriptionReader subscribeReader;
	private final SubscriptionRepository subscribeRepository;

	public SubscriptionActivator(SubscriptionReader subscribeReader, SubscriptionRepository subscribeRepository) {
		this.subscribeReader = subscribeReader;
		this.subscribeRepository = subscribeRepository;
	}

	public void activate(LegislationType legislationType, User user) {
		Subscription subscription = subscribeReader.readSubscription(legislationType, user);
		if (subscription.isActive()) {
			throw SubscriptionDomainException.alreadySubscribed(legislationType);
		}
		subscribeRepository.save(subscription.activate());
	}

	public void deactivate(LegislationType legislationType, User user) {
		Subscription subscription = subscribeReader.readSubscription(legislationType, user);
		if (!subscription.isActive()) {
			throw SubscriptionDomainException.alreadyUnSubscribed(legislationType);
		}
		subscribeRepository.delete(subscription.deactivate());
	}
}
