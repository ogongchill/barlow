package com.barlow.core.domain.subscribe;

import java.util.List;


import com.barlow.core.domain.User;

public interface SubscriptionRepository {

	Subscription retrieve(SubscriptionQuery query);

	List<Subscription> retrieveAll(User user);

	void save(Subscription subscription);

	void delete(Subscription subscription);

	void deleteAll(User user);
}
