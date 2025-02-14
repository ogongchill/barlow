package com.barlow.core.domain.account.notificationsetting;

import org.springframework.stereotype.Component;

@Component
public class NotificationSettingActivator {

	private final NotificationSettingRepository notificationSettingRepository;

	public NotificationSettingActivator(NotificationSettingRepository notificationSettingRepository) {
		this.notificationSettingRepository = notificationSettingRepository;
	}

	public void activate(NotificationSetting notificationSetting) {
		notificationSettingRepository.saveNotificationSetting(notificationSetting.activate());
	}

	public void deactivate(NotificationSetting notificationSetting) {
		notificationSettingRepository.deleteNotificationSetting(notificationSetting.deactivate());
	}
}
