package com.barlow.notification;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class NotificationCenterRegistrar {

	private final NotificationCenterRepository notificationCenterRepository;

	public NotificationCenterRegistrar(NotificationCenterRepository notificationCenterRepository) {
		this.notificationCenterRepository = notificationCenterRepository;
	}

	public void register(NotificationInfo notificationInfo, NotificationRequest request) {
		List<NotificationCenterItemInfo> notificationCenterItemInfos = notificationInfo.getInfos()
			.entrySet()
			.stream()
			.flatMap(entry -> entry.getValue()
				.stream()
				.map(subscriber -> NotificationCenterItemInfo.of(
					subscriber.memberNo(),
					entry.getKey().getTopic(),
					request.billInfosByTopic(entry.getKey().getTopic())
				))
			)
			.toList();
		notificationCenterRepository.registerAll(notificationCenterItemInfos);
	}
}
