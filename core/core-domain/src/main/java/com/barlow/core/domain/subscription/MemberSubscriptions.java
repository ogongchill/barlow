package com.barlow.core.domain.subscription;

import java.util.List;

public class MemberSubscriptions {

    private final List<Subscription> subscriptions;

    public MemberSubscriptions(Long memberNo, List<Subscription> subscriptions) {
        validateMemberNoMismatch(memberNo, subscriptions);
        this.subscriptions = subscriptions;
    }

    private void validateMemberNoMismatch(Long memberNo, List<Subscription> subscriptions) {
        subscriptions.stream()
                .filter(subscription -> !subscription.memberNo().equals(memberNo))
                .findAny()
                .ifPresent(subscription -> {
                    throw MemberSubscriptionException.memberMismatchException(
                            String.format("유효하지 않은 사용지 %d이 조회됨", subscription.memberNo()));
                });
    }

    public boolean hasSubscription(Long targetLegislationAccountNo) {
        return subscriptions.stream()
                .anyMatch(subscription -> subscription.legislationAccountNo().equals(targetLegislationAccountNo));
    }
}
