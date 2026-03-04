package com.barlow.core.service.notificationsetting.impl;

import org.springframework.stereotype.Component;

import com.barlow.core.domain.User;
import com.barlow.core.domain.notificationsetting.NotificationCenterItemRepository;
import com.barlow.core.domain.notificationsetting.NotificationSettingRepository;

@Component
public class NotificationWithdrawalHandler {

	private final NotificationSettingReader notificationSettingReader;
	private final NotificationSettingRepository notificationSettingRepository;
	private final NotificationCenterItemRepository notificationCenterItemRepository;

	public NotificationWithdrawalHandler(NotificationSettingReader notificationSettingReader,
		NotificationSettingRepository notificationSettingRepository,
		NotificationCenterItemRepository notificationCenterItemRepository) {
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
