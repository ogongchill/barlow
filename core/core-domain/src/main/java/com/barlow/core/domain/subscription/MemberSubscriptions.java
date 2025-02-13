package com.barlow.core.domain.subscription;

import java.util.List;

public class MemberSubscriptions {

    private final List<Subscription> subscriptions;

    public MemberSubscriptions(List<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public List<Subscription> retrieveAll() {
        return List.copyOf(subscriptions);
    }
}
