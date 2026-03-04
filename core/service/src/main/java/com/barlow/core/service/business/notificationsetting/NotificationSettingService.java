package com.barlow.core.service.business.notificationsetting;

import org.springframework.stereotype.Service;

import com.barlow.core.domain.User;
import com.barlow.core.enumerate.LegislationType;
import com.barlow.core.service.implement.notificationsetting.NotificationSettingActivator;

@Service
public class NotificationSettingService {

	private final NotificationSettingActivator notificationSettingActivator;

	public NotificationSettingService(NotificationSettingActivator notificationSettingActivator) {
		this.notificationSettingActivator = notificationSettingActivator;
	}

	public void activateSetting(LegislationType type, User user) {
		notificationSettingActivator.activate(type, user);
	}

	public void deactivateSetting(LegislationType type, User user) {
		notificationSettingActivator.deactivate(type, user);
	}
}
