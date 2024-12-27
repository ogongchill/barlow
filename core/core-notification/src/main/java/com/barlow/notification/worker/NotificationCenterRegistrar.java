package com.barlow.notification.worker;

import java.util.List;

import org.springframework.stereotype.Component;

import com.barlow.notification.NotificationCenterRepository;

@Component
public class NotificationCenterRegistrar {

	private final NotificationCenterRepository notificationCenterRepository;

	public NotificationCenterRegistrar(NotificationCenterRepository notificationCenterRepository) {
		this.notificationCenterRepository = notificationCenterRepository;
	}

	public void registerAll(List<NotificationCenterInfo> notificationCenterInfos) {
		notificationCenterRepository.registerAll(notificationCenterInfos);
	}
}
