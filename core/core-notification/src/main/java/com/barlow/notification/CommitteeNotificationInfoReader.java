package com.barlow.notification;

import static com.barlow.notification.NotificationRequest.BillInfo;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.barlow.core.enumerate.NotificationTopic;

@Component
public class CommitteeNotificationInfoReader implements NotificationInfoReader {

	private final NotificationInfoRepository notificationInfoRepository;

	public CommitteeNotificationInfoReader(NotificationInfoRepository notificationInfoRepository) {
		this.notificationInfoRepository = notificationInfoRepository;
	}

	@Override
	public NotificationInfo readNotificationInfos(NotificationRequest request) {
		CommitteeBillNotificationRequest notificationRequest = checkAndConvert(request);
		Map<NotificationTopic, List<BillInfo>> topicsWithBillInfos = notificationRequest.topicsWithBillInfos();
		Set<NotificationTopic> topics = topicsWithBillInfos.keySet();
		NotificationInfo notificationInfos = notificationInfoRepository.retrieveNotificationInfosByTopics(topics);
		topicsWithBillInfos.forEach((topic, billInfos) ->
			notificationInfos.assignBillTotalCountPerTopic(topic, billInfos.size()));
		return notificationInfos;
	}

	private CommitteeBillNotificationRequest checkAndConvert(NotificationRequest request) {
		if (request instanceof CommitteeBillNotificationRequest committeeBillNotificationPayload) {
			return committeeBillNotificationPayload;
		} else {
			throw new IllegalArgumentException("request must be CommitteeBillNotificationRequest");
		}
	}
}
