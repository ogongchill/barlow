package com.barlow.storage.db.core;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.barlow.notification.NotificationInfo;
import com.barlow.notification.NotificationInfoRepository;

@Component
public class NotificationInfoRepositoryAdapter implements NotificationInfoRepository {

	private final NotificationConfigJpaRepository notificationConfigJpaRepository;

	public NotificationInfoRepositoryAdapter(NotificationConfigJpaRepository notificationConfigJpaRepository) {
		this.notificationConfigJpaRepository = notificationConfigJpaRepository;
	}

	@Override
	public List<NotificationInfo> retrieveNotificationInfosByTopic(String topic) {
		return notificationConfigJpaRepository.findAllByEnableTrueAndTopic(NotificationTopic.valueOf(topic))
			.stream()
			.map(projection -> NotificationInfo.initialize(
				projection.memberNo(),
				projection.topic().getValue(),
				projection.deviceOs().name(),
				projection.deviceToken()
			))
			.toList();
	}

	@Override
	public List<NotificationInfo> retrieveNotificationInfosByTopics(Set<String> topics) {
		Set<NotificationTopic> notificationTopics = topics.stream()
			.map(NotificationTopic::valueOf)
			.collect(Collectors.toUnmodifiableSet());
		List<NotificationInfoProjection> projections
			= notificationConfigJpaRepository.findAllByEnableTrueAndTopicIn(notificationTopics);
		return projections.stream()
			.map(projection -> NotificationInfo.initialize(
				projection.memberNo(),
				projection.topic().getValue(),
				projection.deviceOs().name(),
				projection.deviceToken()
			))
			.toList();
	}
}
