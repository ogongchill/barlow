package com.barlow.core.domain.notificationsetting;

import com.barlow.core.domain.User;
import com.barlow.core.enumerate.NotificationTopic;

public class NotificationSetting {

	private final User user;
	private final NotificationTopic notificationTopic;
	private final boolean isNotifiable;

	public NotificationSetting(User user, NotificationTopic notificationTopic, boolean isNotifiable) {
		this.user = user;
		this.notificationTopic = notificationTopic;
		this.isNotifiable = isNotifiable;
	}

	NotificationSetting activate() {
		return new NotificationSetting(user, notificationTopic, true);
	}

	NotificationSetting deactivate() {
		return new NotificationSetting(user, notificationTopic, false);
	}

	public boolean isNotifiable() {
		return isNotifiable;
	}

	public User getUser() {
		return user;
	}

	public NotificationTopic getNotificationTopic() {
		return notificationTopic;
	}

	public String getTopicName() {
		return notificationTopic.getValue();
	}

	public String getIconPath() {
		return notificationTopic.getIconPath();
	}
}
