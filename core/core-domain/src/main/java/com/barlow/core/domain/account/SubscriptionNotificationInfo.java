package com.barlow.core.domain.account;

public record SubscriptionNotificationInfo(
        boolean isSubscribed,
        boolean isNotificationEnabled,
        LegislationAccountInfo legislationAccountInfo
) {
}
