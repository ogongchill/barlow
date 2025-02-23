package com.barlow.storage.db.core;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.barlow.core.enumerate.NotificationTopic;
import com.barlow.notification.NotificationInfo;
import com.barlow.notification.NotificationInfoRepository;

@Component
public class NotificationInfoRepositoryAdapter implements NotificationInfoRepository {

	private final NotificationConfigJpaRepository notificationConfigJpaRepository;

	public NotificationInfoRepositoryAdapter(NotificationConfigJpaRepository notificationConfigJpaRepository) {
		this.notificationConfigJpaRepository = notificationConfigJpaRepository;
	}

	@Override
	public NotificationInfo retrieveNotificationInfosByTopic(String topic) {
		return new NotificationInfo(
			notificationConfigJpaRepository.findAllByEnableTrueAndTopic(NotificationTopic.findByValue(topic))
				.stream()
				.collect(Collectors.groupingBy(
					projection -> NotificationInfo.Topic.initialize(projection.topic().getValue()),
					Collectors.mapping(projection -> new NotificationInfo.Subscriber(
						projection.memberNo(),
						projection.deviceOs().name(),
						projection.deviceToken()
					), Collectors.toList())
				))
		);
	}

	@Override
	public NotificationInfo retrieveNotificationInfosByTopics(Set<String> topics) {
		Set<NotificationTopic> notificationTopics = topics.stream()
			.map(NotificationTopic::findByValue)
			.collect(Collectors.toUnmodifiableSet());
		List<NotificationInfoProjection> projections = notificationConfigJpaRepository
			.findAllByEnableTrueAndTopicIn(notificationTopics);
		return new NotificationInfo(
			projections.stream()
				.collect(Collectors.groupingBy(
					projection -> NotificationInfo.Topic.initialize(projection.topic().getValue()),
					Collectors.mapping(projection -> new NotificationInfo.Subscriber(
						projection.memberNo(),
						projection.deviceOs().name(),
						projection.deviceToken()
					), Collectors.toList())
				))
		);
	}
}
