package com.barlow.core.service.menu;

import java.util.List;

import org.springframework.stereotype.Service;

import com.barlow.core.domain.User;
import com.barlow.core.domain.notificationsetting.NotificationSetting;
import com.barlow.core.service.notificationsetting.impl.NotificationSettingReader;

@Service
public class MenuService {

	private final NotificationSettingReader notificationSettingReader;

	public MenuService(NotificationSettingReader notificationSettingReader) {
		this.notificationSettingReader = notificationSettingReader;
	}

	public List<NotificationSetting> retrieveNotificationSettingMenu(User user) {
		return notificationSettingReader.readNotificationSettings(user);
	}
}
