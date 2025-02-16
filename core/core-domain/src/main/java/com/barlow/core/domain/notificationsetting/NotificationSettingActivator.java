package com.barlow.core.domain.notificationsetting;

import org.springframework.stereotype.Component;

import com.barlow.core.domain.User;

@Component
public class NotificationSettingActivator {

	private final NotificationSettingReader notificationSettingReader;
	private final NotificationSettingRepository notificationSettingRepository;

	public NotificationSettingActivator(
		NotificationSettingReader notificationSettingReader,
		NotificationSettingRepository notificationSettingRepository
	) {
		this.notificationSettingReader = notificationSettingReader;
		this.notificationSettingRepository = notificationSettingRepository;
	}

	public void activate(String committeeName, User user) {
		NotificationSetting notificationSetting = notificationSettingReader.readNotificationSetting(committeeName, user);
		if (notificationSetting.isNotifiable()) {
			throw NotificationSettingDomainException.alreadyRegistered(committeeName);
		}
		notificationSettingRepository.saveNotificationSetting(notificationSetting);
	}

	public void deactivate(String committeeName, User user) {
		NotificationSetting notificationSetting = notificationSettingReader.readNotificationSetting(committeeName, user);
		if (!notificationSetting.isNotifiable()) {
			throw NotificationSettingDomainException.alreadyRegistered(committeeName);
		}
		notificationSettingRepository.deleteNotificationSetting(notificationSetting);
	}
}
