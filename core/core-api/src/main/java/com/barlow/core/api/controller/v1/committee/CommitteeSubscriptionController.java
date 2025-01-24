package com.barlow.core.api.controller.v1.committee;

import com.barlow.core.domain.account.LegislationAccount;
import com.barlow.core.domain.account.LegislationAccountService;
import com.barlow.core.domain.account.LegislationAccounts;
import com.barlow.core.domain.notification.MemberNotificationConfig;
import com.barlow.core.domain.notification.NotificationConfigRetrieveService;
import com.barlow.core.domain.subscription.MemberSubscriptions;
import com.barlow.core.domain.subscription.SubscriptionRetrieveService;
import com.barlow.core.support.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/committee")
public class CommitteeSubscriptionController {

    private final LegislationAccountService legislationAccountService;
    private final SubscriptionRetrieveService subscriptionRetrieveService;
    private final NotificationConfigRetrieveService notificationConfigRetrieveService;

    public CommitteeSubscriptionController(
            LegislationAccountService legislationAccountService,
            SubscriptionRetrieveService subscriptionRetrieveService,
            NotificationConfigRetrieveService notificationConfigRetrieveService
    ) {
        this.legislationAccountService = legislationAccountService;
        this.subscriptionRetrieveService = subscriptionRetrieveService;
        this.notificationConfigRetrieveService = notificationConfigRetrieveService;
    }

    @GetMapping("/infos")
    public ApiResponse<CommitteeSubscriptionNotificationResponse> retrieveCommitteeAccountSubscribeInfo() {
        // 사용자 불러오기 추가
        Long memberNo = null;
        List<CommitteeSubscriptionNotification> committeeNotificationSubscriptions = getCommitteeSubscriptionNotifications(memberNo);
        return ApiResponse.success(new CommitteeSubscriptionNotificationResponse(
                """
                        소관위원회 더 알아보기
                        """,
                """
                        대한민국 상임위원회는 국회 내에서 입법 및 행정에 대한 심의·조사를 담당하는 전문 기구입니다.
                        총 17개 위원회가 있으며, 국회의원들이 소속되어 법률안 심사, 예산안 검토, 국정감사 등을 수행합니다.
                        """,
                committeeNotificationSubscriptions
        ));
    }

    private List<CommitteeSubscriptionNotification> getCommitteeSubscriptionNotifications(Long memberNo) {
        LegislationAccounts committeeAccount = legislationAccountService.retrieveCommitteeAccount();
        MemberSubscriptions subscriptions = subscriptionRetrieveService.retrieveSubscriptionsByMemberNo(memberNo);
        MemberNotificationConfig notificationConfig = notificationConfigRetrieveService.retrieveMemberNotificationConfig(memberNo);
        return committeeAccount.compose(account -> compose(account, subscriptions, notificationConfig));
    }

    private CommitteeSubscriptionNotification compose(
            LegislationAccount legislationAccount,
            MemberSubscriptions memberSubscriptions,
            MemberNotificationConfig memberNotificationConfig) {
        return new CommitteeSubscriptionNotificationApiSpecComposer(legislationAccount)
                .setSubscription(memberSubscriptions)
                .setNotification(memberNotificationConfig)
                .compose();
    }
}