package com.barlow.notification;

import org.springframework.stereotype.Component;

@Component
public class DefaultNotificationInfoReader implements NotificationInfoReader {

	private final NotificationInfoRepository notificationInfoRepository;

	public DefaultNotificationInfoReader(NotificationInfoRepository notificationInfoRepository) {
		this.notificationInfoRepository = notificationInfoRepository;
	}

	@Override
	public NotificationInfo readNotificationInfos(NotificationRequest request) {
		DefaultBillNotificationRequest notificationRequest = checkAndConvert(request);
		NotificationInfo notificationInfo = notificationInfoRepository
			.retrieveNotificationInfosByTopic(notificationRequest.topic());
		notificationInfo.assignRepresentationBillAndTotalCount(
			notificationRequest.representationBill(),
			notificationRequest.totalCount()
		);
		return notificationInfo;
	}

	private DefaultBillNotificationRequest checkAndConvert(NotificationRequest request) {
		if (request instanceof DefaultBillNotificationRequest defaultBillNotificationRequest) {
			return defaultBillNotificationRequest;
		} else {
			throw new IllegalArgumentException("request must be DefaultBillNotificationRequest");
		}
	}
}
