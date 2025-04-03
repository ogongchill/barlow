package com.barlow.storage.db.core.notification;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.barlow.core.enumerate.NotificationTopic;
import com.barlow.notification.NotificationInfo;
import com.barlow.notification.NotificationInfoRepository;

@Component
public class NotificationInfoRepositoryAdapter implements NotificationInfoRepository {

	private final NotificationDispatchConfigJpaRepository notificationConfigJpaRepository;

	public NotificationInfoRepositoryAdapter(NotificationDispatchConfigJpaRepository notificationConfigJpaRepository) {
		this.notificationConfigJpaRepository = notificationConfigJpaRepository;
	}

	@Override
	public NotificationInfo retrieveNotificationInfosByTopic(String topic) {
		return new NotificationInfo(
			notificationConfigJpaRepository.findAllByEnableTrueAndTopic(NotificationTopic.findByValue(topic))
				.stream()
				.collect(Collectors.groupingBy(
					projection -> NotificationInfo.Topic.initialize(projection.topic()),
					Collectors.mapping(projection -> new NotificationInfo.Subscriber(
						projection.memberNo(),
						projection.deviceOs(),
						projection.deviceToken()
					), Collectors.toList())
				))
		);
	}

	@Override
	public NotificationInfo retrieveNotificationInfosByTopics(Set<NotificationTopic> topics) {
		List<NotificationInfoProjection> projections = notificationConfigJpaRepository
			.findAllByEnableTrueAndTopicIn(topics);
		return new NotificationInfo(
			projections.stream()
				.collect(Collectors.groupingBy(
					projection -> NotificationInfo.Topic.initialize(projection.topic()),
					Collectors.mapping(projection -> new NotificationInfo.Subscriber(
						projection.memberNo(),
						projection.deviceOs(),
						projection.deviceToken()
					), Collectors.toList())
				))
		);
	}
}
