package com.barlow.core.domain.notificationsetting;

import org.springframework.stereotype.Component;

import com.barlow.core.domain.User;

@Component
public class NotificationWithdrawalHandler {

	private final NotificationSettingReader notificationSettingReader;
	private final NotificationSettingRepository notificationSettingRepository;

	public NotificationWithdrawalHandler(
		NotificationSettingReader notificationSettingReader,
		NotificationSettingRepository notificationSettingRepository
	) {
		this.notificationSettingReader = notificationSettingReader;
		this.notificationSettingRepository = notificationSettingRepository;
	}

	public void handle(User user) {
		notificationSettingReader.readNotificationSettings(user)
			.forEach(notificationSettingRepository::deleteNotificationSetting);
	}
}
