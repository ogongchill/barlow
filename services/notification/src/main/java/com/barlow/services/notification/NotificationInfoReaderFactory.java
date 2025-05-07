package com.barlow.services.notification;

import org.springframework.stereotype.Component;

@Component
public class NotificationInfoReaderFactory {

	private final DefaultNotificationInfoReader defaultNotificationInfoReader;
	private final CommitteeNotificationInfoReader committeeNotificationInfoReader;

	public NotificationInfoReaderFactory(
		DefaultNotificationInfoReader defaultNotificationInfoReader,
		CommitteeNotificationInfoReader committeeNotificationInfoReader
	) {
		this.defaultNotificationInfoReader = defaultNotificationInfoReader;
		this.committeeNotificationInfoReader = committeeNotificationInfoReader;
	}

	public NotificationInfoReader getBy(NotificationType type) {
		if (type.isCommittee()) {
			return committeeNotificationInfoReader;
		} else {
			return defaultNotificationInfoReader;
		}
	}
}
