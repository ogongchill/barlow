package com.barlow.core.api.controller.v1.committee;

import com.barlow.core.domain.legislationaccount.LegislationAccount;
import com.barlow.core.domain.notification.MemberNotificationConfig;
import com.barlow.core.domain.notification.NotifiableTopic;
import com.barlow.core.domain.notification.NotificationConfig;
import com.barlow.core.domain.subscription.MemberSubscriptions;
import com.barlow.core.domain.subscription.Subscription;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CommitteeSubscriptionNotificationApiSpecComposerTest {

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


    @DisplayName("소관위계정, 알림정보, 구독정보를 api spec에 맞게 변환하는지 확인")
    @ParameterizedTest
    @MethodSource("createExpected")
    void compose(LegislationAccount legislationAccount, Boolean subscriptionExpected, Boolean notificationExpected) {
        MemberSubscriptions subscriptions = createMemberSubscriptions();
        MemberNotificationConfig notificationConfig = createMemberNotificationConfig();
        CommitteeSubscriptionNotification result = new CommitteeSubscriptionNotificationApiSpecComposer(legislationAccount)
                .setSubscription(subscriptions)
                .setNotification(notificationConfig)
                .compose();
        assertAll(
                () -> assertThat(result.isNotificationEnabled()).isEqualTo(notificationExpected),
                () -> assertThat(result.isSubscriptionEnabled()).isEqualTo(subscriptionExpected)
        );
    }

    private static Stream<Arguments> createExpected() {
        return Stream.of(
                Arguments.of(COMMITTEE_ACCOUNT_SAMPLES.get(0), true, true), // subscription = true, notification = true
                Arguments.of(COMMITTEE_ACCOUNT_SAMPLES.get(1), true, false), // subscription = true, notification = false
                Arguments.of(COMMITTEE_ACCOUNT_SAMPLES.get(2), true, false), // subscription = true, notification = null
                Arguments.of(COMMITTEE_ACCOUNT_SAMPLES.get(3), false, false), // subscription = null, notification = false
                Arguments.of(COMMITTEE_ACCOUNT_SAMPLES.get(4), false, true), // subscription = null, notification = true
                Arguments.of(COMMITTEE_ACCOUNT_SAMPLES.get(5), false, false) // subscription = null, notification = null
        );
    }

    private MemberSubscriptions createMemberSubscriptions() {
        return new MemberSubscriptions(
                List.of(
                        new Subscription(1L, MEMBER_NO, 1L),
                        new Subscription(1L, MEMBER_NO, 2L),
                        new Subscription(1L, MEMBER_NO, 3L)
                )
        );
    }

    private MemberNotificationConfig createMemberNotificationConfig() {
        return new MemberNotificationConfig(
                List.of(
                        new NotificationConfig(1L, MEMBER_NO, true, TOPIC_SAMPLES.get(0)),
                        new NotificationConfig(2L, MEMBER_NO, false, TOPIC_SAMPLES.get(1)),
                        new NotificationConfig(4L, MEMBER_NO, false, TOPIC_SAMPLES.get(3)),
                        new NotificationConfig(5L, MEMBER_NO, true, TOPIC_SAMPLES.get(4)),
                        new NotificationConfig(9L, MEMBER_NO, true, TOPIC_SAMPLES.get(8)),
                        new NotificationConfig(10L, MEMBER_NO, true, TOPIC_SAMPLES.get(9))
                )
        );
    }
}