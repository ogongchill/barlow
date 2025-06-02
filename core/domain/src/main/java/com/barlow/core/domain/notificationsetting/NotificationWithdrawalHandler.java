package com.barlow.core.domain.notificationsetting;

import org.springframework.stereotype.Component;

import com.barlow.core.domain.User;

@Component
public class NotificationWithdrawalHandler {

	private final NotificationSettingReader notificationSettingReader;
	private final NotificationSettingRepository notificationSettingRepository;
	private final NotificationCenterItemRepository notificationCenterItemRepository;

	public NotificationWithdrawalHandler(
		NotificationSettingReader notificationSettingReader,
		NotificationSettingRepository notificationSettingRepository,
		NotificationCenterItemRepository notificationCenterItemRepository
	) {
		this.notificationSettingReader = notificationSettingReader;
		this.notificationSettingRepository = notificationSettingRepository;
		this.notificationCenterItemRepository = notificationCenterItemRepository;
	}

	public void handle(User user) {
		notificationCenterItemRepository.deleteAllItems(user);
		notificationSettingReader.readNotificationSettings(user)
			.forEach(notificationSettingRepository::deleteNotificationSetting);
	}
}
