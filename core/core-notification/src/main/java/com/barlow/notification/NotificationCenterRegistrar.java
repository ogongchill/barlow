package com.barlow.notification;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class NotificationCenterRegistrar {

	private final NotificationCenterRepository notificationCenterRepository;

	public NotificationCenterRegistrar(NotificationCenterRepository notificationCenterRepository) {
		this.notificationCenterRepository = notificationCenterRepository;
	}

	public void register(NotificationInfo notificationInfo, NotificationPayload payload) {
		List<NotificationCenterItemInfo> notificationCenterItemInfos = notificationInfo.getInfos()
			.entrySet()
			.stream()
			.flatMap(entry -> entry.getValue()
				.stream()
				.map(subscriber -> NotificationCenterItemInfo.of(
					subscriber.memberNo(),
					entry.getKey().getName(),
					payload.billInfosByTopic(entry.getKey().getName())
				))
			)
			.toList();
		notificationCenterRepository.registerAll(notificationCenterItemInfos);
	}
}
