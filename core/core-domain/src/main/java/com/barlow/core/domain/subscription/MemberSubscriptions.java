package com.barlow.core.domain.subscription;

import java.util.List;

public class MemberSubscriptions {

    private final Long memberNo;
    private final List<Subscription> subscriptions;

    public MemberSubscriptions(Long memberNo, List<Subscription> subscriptions) {
        validateMemberNoMismatch(memberNo, subscriptions);
        this.memberNo = memberNo;
        this.subscriptions = List.copyOf(subscriptions);
    }

    private void validateMemberNoMismatch(Long memberNo, List<Subscription> subscriptions) {
        subscriptions.stream()
                .filter(subscription -> !subscription.memberNo().equals(memberNo))
                .findAny()
                .ifPresent(subscription -> {
                    throw MemberSubscriptionException.memberMismatchException(
                            String.format("memberNo: %d(이)가 아닌 memberNo: %d에 대한 Subscription이 포함되어 있습니다", memberNo, subscription.memberNo()));
                });
    }

    public final List<Subscription> getAll() {
        return List.copyOf(subscriptions);
    }
}
