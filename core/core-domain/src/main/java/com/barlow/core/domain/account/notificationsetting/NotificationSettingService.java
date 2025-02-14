package com.barlow.core.domain.account.notificationsetting;

import org.springframework.stereotype.Service;

import com.barlow.core.domain.User;

@Service
public class NotificationSettingService {

	private final NotificationSettingReader notificationSettingReader;
	private final NotificationSettingActivator notificationSettingActivator;

	public NotificationSettingService(
		NotificationSettingReader notificationSettingReader,
		NotificationSettingActivator notificationSettingActivator
	) {
		this.notificationSettingReader = notificationSettingReader;
		this.notificationSettingActivator = notificationSettingActivator;
	}

	public void activateSetting(String committeeName, User user) {
		NotificationSetting notificationSetting = notificationSettingReader.readNotificationSetting(committeeName, user);
		if (notificationSetting.isNotifiable()) {
			throw NotificationSettingDomainException.alreadyRegistered(committeeName);
		}
		notificationSettingActivator.activate(notificationSetting);
	}

	public void deactivateSetting(String committeeName, User user) {
		NotificationSetting notificationSetting = notificationSettingReader.readNotificationSetting(committeeName, user);
		if (!notificationSetting.isNotifiable()) {
			throw NotificationSettingDomainException.alreadyRegistered(committeeName);
		}
		notificationSettingActivator.deactivate(notificationSetting);
	}
}
