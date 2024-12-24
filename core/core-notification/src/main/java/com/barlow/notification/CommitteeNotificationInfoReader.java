package com.barlow.notification;

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
	public List<NotificationInfo> readNotificationInfos(NotificationPayload payload) {
		CommitteeBillNotificationPayload notificationPayload = checkAndConvert(payload);
		Map<String, Integer> topicsWithCount = notificationPayload.topicsWithCount();
		Set<String> topics = topicsWithCount.keySet();
		List<NotificationInfo> notificationInfos = notificationInfoRepository.retrieveNotificationInfosByTopics(topics);
		for (String topic : topics) {
			notificationInfos.stream()
				.filter(info -> info.isSameTopic(topic))
				.forEach(info -> info.setTopicCount(topicsWithCount.get(topic)));
		}
		return notificationInfos;
	}

	private CommitteeBillNotificationPayload checkAndConvert(NotificationPayload payload) {
		if (payload instanceof CommitteeBillNotificationPayload) {
			return (CommitteeBillNotificationPayload)payload;
		} else {
			throw new IllegalArgumentException("Payload must be CommitteeBillNotificationPayload");
		}
	}
}
