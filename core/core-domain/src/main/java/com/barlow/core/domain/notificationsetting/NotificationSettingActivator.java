package com.barlow.core.domain.notificationsetting;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.barlow.core.domain.User;
import com.barlow.core.enumerate.LegislationType;
import com.barlow.core.enumerate.NotificationTopic;

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

	@Transactional
	public void deactivate(LegislationType type, User user) {
		NotificationSetting notificationSetting = notificationSettingReader.readNotificationSetting(type, user);
		if (!notificationSetting.isNotifiable()) {
			throw NotificationSettingDomainException.alreadyRegistered(type.name());
		}
		notificationSettingRepository.deleteNotificationSetting(notificationSetting.deactivate());
	}

	public void activateDefault(User user) {
		NotificationTopic.findByDefaultTopic()
			.stream()
			.map(topic -> new NotificationSetting(user, topic, true))
			.forEach(notificationSettingRepository::saveNotificationSetting);
	}
}
