package com.barlow.notification;

import static com.barlow.notification.DefaultBillNotificationRequest.BillSummary;

import java.util.Map;
import java.util.Set;

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
		Map<String, BillSummary> topicsWithBillInfos = notificationRequest.topicsWithBillInfos();
		Set<String> topics = topicsWithBillInfos.keySet();
		NotificationInfo notificationInfos = notificationInfoRepository.retrieveNotificationInfosByTopics(topics);
		topicsWithBillInfos.forEach((topic, billSummary) ->
			notificationInfos.assignRepresentationBillAndTotalCountPerTopic(
				topic, billSummary.representationBill(), billSummary.totalCount()
			));
		return notificationInfos;
	}

	private DefaultBillNotificationRequest checkAndConvert(NotificationRequest request) {
		if (request instanceof DefaultBillNotificationRequest defaultBillNotificationRequest) {
			return defaultBillNotificationRequest;
		} else {
			throw new IllegalArgumentException("request must be DefaultBillNotificationRequest");
		}
	}
}
