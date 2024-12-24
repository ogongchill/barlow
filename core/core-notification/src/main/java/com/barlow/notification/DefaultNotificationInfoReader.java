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
	public List<NotificationInfo> readNotificationInfos(NotificationPayload payload) {
		DefaultBillNotificationPayload notificationPayload = checkAndConvert(payload);
		List<NotificationInfo> notificationInfos = notificationInfoRepository.retrieveNotificationInfosByTopic(notificationPayload.topic());
		notificationInfos.forEach(info -> {
			info.setRepresentation(notificationPayload.representationBill());
			info.setTopicCount(notificationPayload.totalCount());
		});
		return notificationInfos;
	}

	private DefaultBillNotificationPayload checkAndConvert(NotificationPayload payload) {
		if (payload instanceof DefaultBillNotificationPayload) {
			return (DefaultBillNotificationPayload)payload;
		} else {
			throw new IllegalArgumentException("Payload must be DefaultBillNotificationPayload");
		}
	}
}
