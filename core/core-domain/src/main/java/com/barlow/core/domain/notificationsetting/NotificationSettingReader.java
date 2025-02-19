package com.barlow.core.domain.notificationsetting;

import java.util.List;

import org.springframework.stereotype.Component;

import com.barlow.core.domain.User;
import com.barlow.core.enumerate.LegislationType;

@Component
public class NotificationSettingReader {

	private final NotificationSettingRepository notificationSettingRepository;

	public NotificationSettingReader(NotificationSettingRepository notificationSettingRepository) {
		this.notificationSettingRepository = notificationSettingRepository;
	}

	public NotificationSetting readNotificationSetting(LegislationType type, User user) {
		LegislationNotificationSettingQuery query = new LegislationNotificationSettingQuery(type, user);
		return notificationSettingRepository.retrieveNotificationSetting(query);
	}

	public List<NotificationSetting> readNotificationSettings(User user) {
		return notificationSettingRepository.retrieveNotificationSettings(user);
	}
}
