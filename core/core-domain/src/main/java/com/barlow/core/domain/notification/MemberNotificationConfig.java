package com.barlow.core.domain.notification;

import java.util.List;

public class MemberNotificationConfig {

    private final List<NotificationConfig> notificationConfigs;

    public MemberNotificationConfig(List<NotificationConfig> notificationConfigs) {
        this.notificationConfigs = notificationConfigs;
    }

    public List<NotificationConfig> retrieveAll() {
        return List.copyOf(notificationConfigs);
    }
}
