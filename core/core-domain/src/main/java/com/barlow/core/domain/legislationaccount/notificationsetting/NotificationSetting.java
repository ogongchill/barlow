package com.barlow.core.domain.legislationaccount.notificationsetting;

import com.barlow.core.domain.User;

public class NotificationSetting {

	private final User user;
	private final NotificationTopic notificationTopic;
	private final boolean isNotifiable;

	public NotificationSetting(User user, String name, String iconPath, boolean isNotifiable) {
		this.user = user;
		this.notificationTopic = new NotificationTopic(name, iconPath);
		this.isNotifiable = isNotifiable;
	}

	private NotificationSetting(User user, NotificationTopic notificationTopic, boolean isNotifiable) {
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

	boolean isNotifiable() {
		return isNotifiable;
	}

	public User getUser() {
		return user;
	}

	public String getTopicName() {
		return notificationTopic.korName;
	}

	public String getIconPath() {
		return notificationTopic.iconPath;
	}

	static class NotificationTopic {

		private final String korName;
		private final String iconPath;

		public NotificationTopic(String korName, String iconPath) {
			this.korName = korName;
			this.iconPath = iconPath;
		}
	}
}
