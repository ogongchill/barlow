package com.barlow.notification;

import org.springframework.stereotype.Component;

@Component
public class DefaultNotificationInfoReader implements NotificationInfoReader {

	private final NotificationInfoRepository notificationInfoRepository;

	public DefaultNotificationInfoReader(NotificationInfoRepository notificationInfoRepository) {
		this.notificationInfoRepository = notificationInfoRepository;
	}

	@Override
	public NotificationInfo readNotificationInfos(NotificationPayload payload) {
		DefaultBillNotificationPayload notificationPayload = checkAndConvert(payload);
		NotificationInfo notificationInfo = notificationInfoRepository
			.retrieveNotificationInfosByTopic(notificationPayload.topic());
		notificationInfo.assignRepresentationBillAndTotalCount(
			notificationPayload.representationBill(),
			notificationPayload.totalCount()
		);
		return notificationInfo;
	}

	private DefaultBillNotificationPayload checkAndConvert(NotificationPayload payload) {
		if (payload instanceof DefaultBillNotificationPayload defaultBillNotificationPayload) {
			return defaultBillNotificationPayload;
		} else {
			throw new IllegalArgumentException("Payload must be DefaultBillNotificationPayload");
		}
	}
}
