package com.barlow.notification;

import static com.barlow.notification.NotificationPayload.BillInfo;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

@Component
public class CommitteeNotificationInfoReader implements NotificationInfoReader {

	private final NotificationInfoRepository notificationInfoRepository;

	public CommitteeNotificationInfoReader(NotificationInfoRepository notificationInfoRepository) {
		this.notificationInfoRepository = notificationInfoRepository;
	}

	@Override
	public NotificationInfo readNotificationInfos(NotificationPayload payload) {
		CommitteeBillNotificationPayload notificationPayload = checkAndConvert(payload);
		Map<String, List<BillInfo>> topicsWithBillInfos = notificationPayload.topicsWithBillInfos();
		Set<String> topics = topicsWithBillInfos.keySet();
		NotificationInfo notificationInfos = notificationInfoRepository.retrieveNotificationInfosByTopics(topics);
		topicsWithBillInfos.forEach((topic, billInfos) ->
			notificationInfos.assignBillTotalCountPerTopic(topic, billInfos.size()));
		return notificationInfos;
	}

	private CommitteeBillNotificationPayload checkAndConvert(NotificationPayload payload) {
		if (payload instanceof CommitteeBillNotificationPayload committeeBillNotificationPayload) {
			return committeeBillNotificationPayload;
		} else {
			throw new IllegalArgumentException("Payload must be CommitteeBillNotificationPayload");
		}
	}
}
