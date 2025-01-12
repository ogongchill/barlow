package com.barlow.core.domain.committee;

import com.barlow.core.domain.account.LegislationAccount;
import com.barlow.core.domain.notification.MemberNotificationConfig;
import com.barlow.core.domain.notification.NotificationConfig;
import com.barlow.core.domain.subscription.MemberSubscriptions;

public class CommitteeNotificationSubscriptionComposer {

    private final LegislationAccount legislationAccount;
    private CommitteeNotificationSubscription.Builder builder;

    public CommitteeNotificationSubscriptionComposer(LegislationAccount legislationAccount) {
        this.legislationAccount = legislationAccount;
        this.builder = CommitteeNotificationSubscription.builder()
                .legislationAccountNo(legislationAccount.no())
                .name(legislationAccount.name())
                .iconUrl(legislationAccount.iconUrl());
    }

    public CommitteeNotificationSubscription compose() {
        return builder.build();
    }

    public CommitteeNotificationSubscriptionComposer setSubscription(MemberSubscriptions memberSubscriptions) {
        if (memberSubscriptions.hasSubscription(legislationAccount.no())) {
            builder = builder.isSubscriptionEnable(true);
            return this;
        }
        builder = builder.isSubscriptionEnable(false);
        return this;
    }

    public CommitteeNotificationSubscriptionComposer setNotification(MemberNotificationConfig memberNotificationConfig) {
        NotificationConfig targetNotificationConfig = memberNotificationConfig.findByTopicName(legislationAccount.name());
        builder = builder.isNotificationEnable(targetNotificationConfig.isEnable());
        return this;
    }
}
