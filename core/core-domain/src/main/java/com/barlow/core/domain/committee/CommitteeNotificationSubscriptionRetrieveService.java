package com.barlow.core.domain.committee;

import com.barlow.core.domain.account.LegislationAccount;
import com.barlow.core.domain.account.LegislationAccountService;
import com.barlow.core.domain.account.LegislationAccounts;
import com.barlow.core.domain.notification.MemberNotificationConfig;
import com.barlow.core.domain.notification.NotificationConfig;
import com.barlow.core.domain.notification.NotificationConfigRetrieveService;
import com.barlow.core.domain.subscription.MemberSubscriptions;
import com.barlow.core.domain.subscription.SubscriptionRetrieveService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommitteeNotificationSubscriptionRetrieveService {

    private final NotificationConfigRetrieveService notificationConfigRetrieveService;
    private final SubscriptionRetrieveService subscriptionRetrieveService;
    private final LegislationAccountService legislationAccountService;

    public CommitteeNotificationSubscriptionRetrieveService(
            NotificationConfigRetrieveService notificationConfigRetrieveService,
            SubscriptionRetrieveService subscriptionRetrieveService,
            LegislationAccountService legislationAccountService) {
        this.notificationConfigRetrieveService = notificationConfigRetrieveService;
        this.subscriptionRetrieveService = subscriptionRetrieveService;
        this.legislationAccountService = legislationAccountService;
    }

    public List<CommitteeNotificationSubscription> retrieveByMemberNo(Long memberNo) {
        LegislationAccounts committeeAccount = legislationAccountService.retrieveCommitteeAccount();
        MemberNotificationConfig memberNotificationConfig = notificationConfigRetrieveService.retrieveMemberNotificationConfig(memberNo);
        MemberSubscriptions memberSubscriptions = subscriptionRetrieveService.retrieveSubscriptionsByMemberNo(memberNo);
        return committeeAccount.map(legislationAccount ->
                compose(legislationAccount, memberSubscriptions, memberNotificationConfig)
        );
    }

    private CommitteeNotificationSubscription compose(
            LegislationAccount legislationAccount,
            MemberSubscriptions memberSubscriptions,
            MemberNotificationConfig memberNotificationConfig) {
        CommitteeNotificationSubscriptionComposer composer = new CommitteeNotificationSubscriptionComposer(legislationAccount);
        return composer.accept(builder -> {
            if (memberSubscriptions.hasSubscription(legislationAccount.no())) {
                builder.isSubscriptionEnable(true);
                return;
            }
            builder.isSubscriptionEnable(false);
        }).accept(builder -> {
            NotificationConfig notificationConfig = memberNotificationConfig.findByTopicName(legislationAccount.name());
            builder.isNotificationEnable(notificationConfig.isEnable());
        }).compose();
    }
}
