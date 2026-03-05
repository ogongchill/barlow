package com.barlow.core.service.subscribe;

import java.util.List;

import org.springframework.stereotype.Component;

import com.barlow.core.domain.User;
import com.barlow.core.domain.subscribe.Subscription;
import com.barlow.core.domain.subscribe.SubscriptionQuery;
import com.barlow.core.domain.subscribe.SubscriptionRepository;
import com.barlow.core.enumerate.LegislationType;

@Component
public class SubscriptionReader {

	private final SubscriptionRepository subscribeRepository;

	public SubscriptionReader(SubscriptionRepository subscribeRepository) {
		this.subscribeRepository = subscribeRepository;
	}

	public Subscription readSubscription(LegislationType legislationType, User user) {
		SubscriptionQuery query = new SubscriptionQuery(legislationType, user);
		return subscribeRepository.retrieve(query);
	}

	public List<Subscription> readSubscriptions(User user) {
		return subscribeRepository.retrieveAll(user);
	}
}
