package com.barlow.core.domain.notification;

import java.util.List;
import java.util.NoSuchElementException;

public class MemberNotificationConfig {

    private final List<NotificationConfig> notificationConfigs;

    public MemberNotificationConfig(List<NotificationConfig> notificationConfigs) {
        validateSameMemberNo(notificationConfigs);
        this.notificationConfigs = notificationConfigs;
    }

    private void validateSameMemberNo(List<NotificationConfig> notificationConfigs) {
        Long targetMemberNo = notificationConfigs.getFirst().memberNo();
        boolean hasDifferentMemberNo = notificationConfigs.stream()
                .anyMatch(notificationConfig -> !notificationConfig.memberNo().equals(targetMemberNo));
        if(hasDifferentMemberNo) {
            throw new IllegalArgumentException("has differentMemberNo");
        }
    }

    public NotificationConfig findByTopicName(String targetName) {
       return  notificationConfigs.stream()
                .filter(notificationConfig -> notificationConfig.topic().name().equals(targetName))
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
    }
}
