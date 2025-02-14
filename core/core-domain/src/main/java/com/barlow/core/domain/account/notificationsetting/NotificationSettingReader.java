package com.barlow.core.domain.account.notificationsetting;

import java.util.List;

import org.springframework.stereotype.Component;

import com.barlow.core.domain.User;

@Component
public class NotificationSettingReader {

	private final NotificationSettingRepository notificationSettingRepository;

	public NotificationSettingReader(
		NotificationSettingRepository notificationSettingRepository) {
		this.notificationSettingRepository = notificationSettingRepository;
	}

	public NotificationSetting readNotificationSetting(String committeeName, User user) {
		LegislationNotificationSettingQuery query = new LegislationNotificationSettingQuery(committeeName, user);
		return notificationSettingRepository.retrieveNotificationSetting(query);
	}

	public List<NotificationSetting> readNotificationSettings(User user) {
		return notificationSettingRepository.retrieveNotificationSettings(user);
	}
}
