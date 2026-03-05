package com.barlow.core.domain.notificationsetting;

import com.barlow.core.enumerate.NotificationTopic;

public class NotificationSetting {

	private final long userNo;
	private final NotificationTopic notificationTopic;
	private final boolean isNotifiable;

	public NotificationSetting(long userNo, NotificationTopic notificationTopic, boolean isNotifiable) {
		this.userNo = userNo;
		this.notificationTopic = notificationTopic;
		this.isNotifiable = isNotifiable;
	}

	public NotificationSetting activate() {
		return new NotificationSetting(userNo, notificationTopic, true);
	}

	public NotificationSetting deactivate() {
		return new NotificationSetting(userNo, notificationTopic, false);
	}

	public boolean isNotifiable() {
		return isNotifiable;
	}

	public long getUserNo() {
		return userNo;
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
