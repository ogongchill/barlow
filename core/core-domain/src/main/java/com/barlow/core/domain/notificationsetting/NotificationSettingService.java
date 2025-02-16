package com.barlow.core.domain.notificationsetting;

import org.springframework.stereotype.Service;

import com.barlow.core.domain.User;

@Service
public class NotificationSettingService {

	private final NotificationSettingActivator notificationSettingActivator;

	public NotificationSettingService(NotificationSettingActivator notificationSettingActivator) {
		this.notificationSettingActivator = notificationSettingActivator;
	}

	public void activateSetting(String committeeName, User user) {
		notificationSettingActivator.activate(committeeName, user);
	}

	public void deactivateSetting(String committeeName, User user) {
		notificationSettingActivator.deactivate(committeeName, user);
	}
}
