package com.barlow.core.api.controller.v1.committee;

import com.barlow.core.domain.account.LegislationAccount;
import com.barlow.core.domain.notification.MemberNotificationConfig;
import com.barlow.core.domain.subscription.MemberSubscriptions;

public class CommitteeSubscriptionNotificationApiSpecComposer {

    private final LegislationAccount legislationAccount;
    private CommitteeSubscriptionNotification.Builder builder;

    public CommitteeSubscriptionNotificationApiSpecComposer(LegislationAccount legislationAccount) {
        this.legislationAccount = legislationAccount;
        this.builder = CommitteeSubscriptionNotification.builder()
                .legislationAccountNo(legislationAccount.no())
                .accountName(legislationAccount.name())
                .iconUrl(legislationAccount.iconUrl());
    }

    public CommitteeSubscriptionNotification compose() {
        return builder.build();
    }

    public CommitteeSubscriptionNotificationApiSpecComposer setSubscription(MemberSubscriptions memberSubscriptions) {
        memberSubscriptions.getAll()
                .stream()
                .filter(subscription -> subscription.legislationAccountNo().equals(legislationAccount.no()))
                .findAny()
                .ifPresentOrElse(
                        unused -> builder = builder.isSubscriptionEnabled(true),
                        () -> builder = builder.isSubscriptionEnabled(false)
                );
        return this;
    }

    public CommitteeSubscriptionNotificationApiSpecComposer setNotification(MemberNotificationConfig memberNotificationConfig) {
        memberNotificationConfig.getAll()
                .stream()
                .filter(notificationConfig -> notificationConfig.topic().korName().equals(legislationAccount.name()))
                .findAny()
                .ifPresentOrElse(
                        notificationConfig -> builder = builder.isNotificationEnabled(notificationConfig.isEnable()),
                        () -> builder = builder.isNotificationEnabled(false)
                );
        return this;
    }
}
