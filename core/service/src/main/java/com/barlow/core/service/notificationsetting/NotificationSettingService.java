package com.barlow.core.service.notificationsetting;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.barlow.core.domain.User;
import com.barlow.core.enumerate.LegislationType;
import com.barlow.core.service.notificationsetting.impl.NotificationSettingActivator;

@Service
public class NotificationSettingService {

	private final NotificationSettingActivator notificationSettingActivator;

	public NotificationSettingService(NotificationSettingActivator notificationSettingActivator) {
		this.notificationSettingActivator = notificationSettingActivator;
	}

	@Transactional
	public void activateSetting(LegislationType type, User user) {
		notificationSettingActivator.activate(type, user);
	}

	@Transactional
	public void deactivateSetting(LegislationType type, User user) {
		notificationSettingActivator.deactivate(type, user);
	}
}
