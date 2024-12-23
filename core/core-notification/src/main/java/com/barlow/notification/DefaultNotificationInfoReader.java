package com.barlow.notification;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class DefaultNotificationInfoReader implements NotificationInfoReader {

	private final NotificationInfoRepository notificationInfoRepository;

	public DefaultNotificationInfoReader(NotificationInfoRepository notificationInfoRepository) {
		this.notificationInfoRepository = notificationInfoRepository;
	}

	@Override
	public List<NotificationInfo> readNotificationInfos(BillNotificationPayload payload) {
		List<NotificationInfo> notificationInfos = notificationInfoRepository.retrieveNotificationInfosByTopic(payload.topic());
		notificationInfos.forEach(info -> {
			info.setRepresentation(payload.representationBill());
			info.setTopicCount(payload.totalCount());
		});
		return notificationInfos;
	}
}
