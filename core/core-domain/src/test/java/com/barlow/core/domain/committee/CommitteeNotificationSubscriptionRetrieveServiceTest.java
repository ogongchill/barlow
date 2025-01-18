package com.barlow.core.domain.committee;

import com.barlow.core.domain.account.LegislationAccount;
import com.barlow.core.domain.account.LegislationAccountService;
import com.barlow.core.domain.account.LegislationAccounts;
import com.barlow.core.domain.notification.*;
import com.barlow.core.domain.subscription.MemberSubscriptions;
import com.barlow.core.domain.subscription.Subscription;
import com.barlow.core.domain.subscription.SubscriptionRetrieveService;
import com.barlow.core.exception.CoreDomainException;
import com.barlow.core.exception.CoreDomainExceptionCode;
import com.barlow.core.exception.CoreDomainExceptionLevel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = CommitteeNotificationSubscriptionRetrieveService.class)
class CommitteeNotificationSubscriptionRetrieveServiceTest {

    private static final Long MEMBER_NO = 1L;
    private static final List<NotifiableTopic> TOPIC_SAMPLES = List.of(
            new NotifiableTopic("HOUSE_STEERING", "국회운영위원회", "default/icon-image-url"),
            new NotifiableTopic("NATIONAL_POLICY", "정무위원회", "default/icon-image-url"),
            new NotifiableTopic("STRATEGY_AND_FINANCE", "기획재정위원회", "default/icon-image-url"),
            new NotifiableTopic("SCIENCE_ICT_BROADCASTING_AND_COMMUNICATIONS", "과학기술정보방송통신위원회", "default/icon-image-url"),
            new NotifiableTopic("FOREIGN_AFFAIRS_AND_UNIFICATION", "외교통일위원회", "default/icon-image-url"),
            new NotifiableTopic("NATIONAL_DEFENSE", "국방위원회", "default/icon-image-url"),
            new NotifiableTopic("PUBLIC_ADMINISTRATION_AND_SECURITY", "행정안전위원회", "default/icon-image-url"),
            new NotifiableTopic("CULTURE_SPORTS_AND_TOURISM", "문화체육관광위원회", "default/icon-image-url"),
            new NotifiableTopic("REACTION", "리액션", "default/icon-image-url"),
            new NotifiableTopic("COMMENT", "댓글", "default/icon-image-url"));
    private static final List<LegislationAccount> COMMITTEE_ACCOUNT_SAMPLES = List.of(
            new LegislationAccount(1L, "국회운영위원회", "default/icon-image-url", "", 0, 0),
            new LegislationAccount(2L, "정무위원회", "default/icon-image-url", "", 0, 0),
            new LegislationAccount(3L, "기획재정위원회", "default/icon-image-url", "", 0, 0),
            new LegislationAccount(4L, "과학기술정보방송통신위원회", "default/icon-image-url", "", 0, 0),
            new LegislationAccount(5L, "외교통일위원회", "default/icon-image-url", "", 0, 0),
            new LegislationAccount(6L, "국방위원회", "default/icon-image-url", "", 0, 0),
            new LegislationAccount(7L, "행정안전위원회", "default/icon-image-url", "", 0, 0),
            new LegislationAccount(8L, "문화체육관광위원회", "default/icon-image-url", "", 0, 0));

    @MockitoBean
    private SubscriptionRetrieveService subscriptionRetrieveService;

    @MockitoBean
    private LegislationAccountService legislationAccountService;

    @MockitoBean
    private NotificationConfigRetrieveService notificationConfigRetrieveService;

    @Autowired
    private CommitteeNotificationSubscriptionRetrieveService committeeNotificationSubscriptionRetrieveService;

    @BeforeEach
    void mockRetrieveCommitteeAccount() {
        when(legislationAccountService.retrieveCommitteeAccount())
                .thenReturn(new LegislationAccounts(COMMITTEE_ACCOUNT_SAMPLES));
    }

    @DisplayName("소관위 알림 구독 정보를 반환하는지 확인")
    @Test
    void retrieveByMemberNo() {
        mockRetrieve();
        List<CommitteeNotificationSubscription> result = committeeNotificationSubscriptionRetrieveService.retrieveByMemberNo(MEMBER_NO);
        assertThat(result)
                .contains(new CommitteeNotificationSubscription(1L, "국회운영위원회", "default/icon-image-url", true, true),
                        new CommitteeNotificationSubscription(2L, "정무위원회", "default/icon-image-url", true, true),
                        new CommitteeNotificationSubscription(3L, "기획재정위원회", "default/icon-image-url", true, true),
                        new CommitteeNotificationSubscription(4L, "과학기술정보방송통신위원회", "default/icon-image-url", true, false),
                        new CommitteeNotificationSubscription(5L, "외교통일위원회", "default/icon-image-url", false, false),
                        new CommitteeNotificationSubscription(6L, "국방위원회", "default/icon-image-url", false, true),
                        new CommitteeNotificationSubscription(7L, "행정안전위원회", "default/icon-image-url", false, true),
                        new CommitteeNotificationSubscription(8L, "문화체육관광위원회", "default/icon-image-url", false, true))
                .hasSize(8);
    }

    private void mockRetrieve() {
        when(subscriptionRetrieveService.retrieveSubscriptionsByMemberNo(MEMBER_NO))
                .thenReturn(new MemberSubscriptions(MEMBER_NO,
                        List.of(new Subscription(1L, MEMBER_NO, 1L),
                                new Subscription(2L, MEMBER_NO, 2L),
                                new Subscription(3L, MEMBER_NO, 3L),
                                new Subscription(4L, MEMBER_NO, 4L))
                ));
        when(notificationConfigRetrieveService.retrieveMemberNotificationConfig(MEMBER_NO))
                .thenReturn(new MemberNotificationConfig(MEMBER_NO,
                        List.of(new NotificationConfig(1L, MEMBER_NO, true, TOPIC_SAMPLES.get(0)),
                                new NotificationConfig(2L, MEMBER_NO, true, TOPIC_SAMPLES.get(1)),
                                new NotificationConfig(3L, MEMBER_NO, true, TOPIC_SAMPLES.get(2)),
                                new NotificationConfig(4L, MEMBER_NO, false, TOPIC_SAMPLES.get(3)),
                                new NotificationConfig(5L, MEMBER_NO, false, TOPIC_SAMPLES.get(4)),
                                new NotificationConfig(6L, MEMBER_NO, true, TOPIC_SAMPLES.get(5)),
                                new NotificationConfig(7L, MEMBER_NO, true, TOPIC_SAMPLES.get(6)),
                                new NotificationConfig(8L, MEMBER_NO, true, TOPIC_SAMPLES.get(7)),
                                new NotificationConfig(9L, MEMBER_NO, true, TOPIC_SAMPLES.get(8)),
                                new NotificationConfig(10L, MEMBER_NO, true, TOPIC_SAMPLES.get(9))
                        )
                ));
    }

    @DisplayName("MemberNotificationConfig에서 CommitteAccount에 관한 알림 정보가 없으면 404 예외 발생")
    @Test
    void throwExceptionWhenNoCommitteeNotificationConfig() {
        mockHasNoCommitteeNotificationConfig();
        assertThatThrownBy(() -> committeeNotificationSubscriptionRetrieveService.retrieveByMemberNo(MEMBER_NO))
                .isInstanceOf(MemberNotificationConfigException.class)
                .hasMessageContaining("국방위원회")
                .satisfies(rawException -> {
                            CoreDomainException exception = (CoreDomainException) rawException;
                            assertThat(exception.getCode()).isEqualTo(CoreDomainExceptionCode.E404);
                            assertThat(exception.getLevel()).isEqualTo(CoreDomainExceptionLevel.IMPLEMENTATION);
                        }
                );
    }

    private void mockHasNoCommitteeNotificationConfig() {
        when(subscriptionRetrieveService.retrieveSubscriptionsByMemberNo(MEMBER_NO))
                .thenReturn(new MemberSubscriptions(MEMBER_NO, new ArrayList<Subscription>()));
        when(notificationConfigRetrieveService.retrieveMemberNotificationConfig(MEMBER_NO))
                .thenReturn(new MemberNotificationConfig(MEMBER_NO,
                                List.of( // 국방위원회 정보 없음
                                        new NotificationConfig(1L, MEMBER_NO, true, TOPIC_SAMPLES.get(0)),
                                        new NotificationConfig(2L, MEMBER_NO, true, TOPIC_SAMPLES.get(1)),
                                        new NotificationConfig(3L, MEMBER_NO, true, TOPIC_SAMPLES.get(2)),
                                        new NotificationConfig(4L, MEMBER_NO, false, TOPIC_SAMPLES.get(3)),
                                        new NotificationConfig(5L, MEMBER_NO, false, TOPIC_SAMPLES.get(4)),
                                        new NotificationConfig(7L, MEMBER_NO, true, TOPIC_SAMPLES.get(6)),
                                        new NotificationConfig(8L, MEMBER_NO, true, TOPIC_SAMPLES.get(7)),
                                        new NotificationConfig(9L, MEMBER_NO, true, TOPIC_SAMPLES.get(8)),
                                        new NotificationConfig(10L, MEMBER_NO, true, TOPIC_SAMPLES.get(9))
                                )
                        )
                );
    }
}