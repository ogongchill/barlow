package com.barlow.core.api.controller.v1.committee;

import java.util.List;

public record CommitteeSubscriptionResponse(
        String title,
        String description,
        List<AccountInfo> legislations
) {
    record AccountInfo(
            Long legislationAccountId,
            String legislationAccount,
            String iconUrl,
            String isSubscribed,
            String isNotificationEnabled
    ) {
    }
}