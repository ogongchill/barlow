package com.barlow.services.notification;

public interface NotificationInfoReader {

	NotificationInfo readNotificationInfos(NotificationRequest request, int page);

	NotificationType supportedType();
}
