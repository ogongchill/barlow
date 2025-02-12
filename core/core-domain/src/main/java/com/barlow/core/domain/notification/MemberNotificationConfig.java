package com.barlow.core.domain.notification;

import java.util.List;

public class MemberNotificationConfig {

    private final Long memberNo;
    private final List<NotificationConfig> notificationConfigs;

    public MemberNotificationConfig(Long memberNo, List<NotificationConfig> notificationConfigs) {
        validateMemberMismatch(memberNo, notificationConfigs);
        this.memberNo = memberNo;
        this.notificationConfigs = List.copyOf(notificationConfigs);
    }

    private void validateMemberMismatch(Long memberNo, List<NotificationConfig> notificationConfigs) {
        notificationConfigs.stream()
                .filter(notificationConfig -> !notificationConfig.memberNo().equals(memberNo))
                .findAny()
                .ifPresent(notificationConfig -> {
                            throw MemberNotificationConfigException.memberMismatchException(
                                    String.format("memberNo: %d(이)가 아닌 memberNo: %d에 대한 NotificationConfig이 포함되어 있습니다", memberNo, notificationConfig.memberNo()));
                        }
                );
    }

    public List<NotificationConfig> getAll() {
        return List.copyOf(notificationConfigs);
    }
}
