package com.barlow.core.api.controller.v1.committee;

import com.barlow.core.domain.account.LegislationAccount;
import com.barlow.core.domain.notification.MemberNotificationConfig;
import com.barlow.core.domain.notification.NotificationConfig;
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
        if (memberSubscriptions.hasSubscription(legislationAccount.no())) {
            builder = builder.isSubscriptionEnabled(true);
            return this;
        }
        builder = builder.isSubscriptionEnabled(false);
        return this;
    }

    public CommitteeSubscriptionNotificationApiSpecComposer setNotification(MemberNotificationConfig memberNotificationConfig) {
        if (memberNotificationConfig.hasTopicName(legislationAccount.name())) {
            NotificationConfig targetNotificationConfig = memberNotificationConfig.findByTopicName(legislationAccount.name());
            builder = builder.isNotificationEnabled(targetNotificationConfig.isEnable());
            return this;
        }
        builder = builder.isNotificationEnabled(false);
        return this;
    }
}
