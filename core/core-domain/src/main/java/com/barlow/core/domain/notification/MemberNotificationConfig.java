package com.barlow.core.domain.notification;

import java.util.List;

public class MemberNotificationConfig {

    private final List<NotificationConfig> notificationConfigs;

    public MemberNotificationConfig(Long memberNo, List<NotificationConfig> notificationConfigs) {
        validateNotEmpty(memberNo, notificationConfigs);
        validateMemberNoMismatch(memberNo, notificationConfigs);
        this.notificationConfigs = notificationConfigs;
    }

    private void validateNotEmpty(Long memberNo, List<NotificationConfig> notificationConfigs) {
        if(notificationConfigs.isEmpty()) {
            throw MemberNotificationConfigException.emptyNotification(memberNo);
        }
    }

    private void validateMemberNoMismatch(Long memberNo, List<NotificationConfig> notificationConfigs) {
        notificationConfigs.stream()
                .filter(notificationConfig -> !notificationConfig.memberNo().equals(memberNo))
                .findAny()
                .ifPresent(subscription -> {
                    throw MemberNotificationConfigException.memberMismatchException(
                            String.format("유효하지 않은 사용지 %l이 조회됨", subscription.memberNo()));
                });
    }

    public NotificationConfig findByTopicName(String targetName) {
        return notificationConfigs.stream()
                .filter(notificationConfig -> notificationConfig.topic().name().equals(targetName))
                .findFirst()
                .orElseThrow(() -> MemberNotificationConfigException.notFoundTopicName(
                        notificationConfigs.getFirst().memberNo(), targetName));
    }
}
