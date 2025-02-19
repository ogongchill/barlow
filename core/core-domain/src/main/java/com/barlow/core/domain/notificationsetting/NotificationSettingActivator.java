package com.barlow.core.domain.notificationsetting;

import org.springframework.stereotype.Component;

import com.barlow.core.domain.User;
import com.barlow.core.enumerate.LegislationType;

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

	public void activate(LegislationType type, User user) {
		NotificationSetting notificationSetting = notificationSettingReader.readNotificationSetting(type, user);
		if (notificationSetting.isNotifiable()) {
			throw NotificationSettingDomainException.alreadyRegistered(type.name());
		}
		notificationSettingRepository.saveNotificationSetting(notificationSetting.activate());
	}

	public void deactivate(LegislationType type, User user) {
		NotificationSetting notificationSetting = notificationSettingReader.readNotificationSetting(type, user);
		if (!notificationSetting.isNotifiable()) {
			throw NotificationSettingDomainException.alreadyRegistered(type.name());
		}
		notificationSettingRepository.deleteNotificationSetting(notificationSetting.deactivate());
	}
}
