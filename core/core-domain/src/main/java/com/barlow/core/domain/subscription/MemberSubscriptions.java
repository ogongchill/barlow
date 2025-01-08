package com.barlow.core.domain.subscription;

import java.util.List;

public class MemberSubscriptions {

    private final List<Subscription> subscriptions;

    public MemberSubscriptions(List<Subscription> subscriptions) {
        validate(subscriptions);
        this.subscriptions = subscriptions;
    }

    private void validate(List<Subscription> subscriptions) {
        Long targetMemberNo = subscriptions.getFirst().memberNo();
        boolean hasDifferentMemberNo = subscriptions.stream()
                .anyMatch(subscription -> !subscription.memberNo().equals(targetMemberNo));
        if(hasDifferentMemberNo) {
            throw new IllegalArgumentException("has different member no");
        }
    }
}
