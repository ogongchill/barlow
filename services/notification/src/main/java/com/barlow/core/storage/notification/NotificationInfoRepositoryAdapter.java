package com.barlow.core.storage.notification;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.barlow.core.enumerate.NotificationTopic;
import com.barlow.services.notification.NotificationInfo;
import com.barlow.services.notification.NotificationInfoRepository;

@Component
public class NotificationInfoRepositoryAdapter implements NotificationInfoRepository {

	private static final Sort DEFAULT_SORT = Sort.by(Sort.Direction.ASC, "nc.no");
	private static final int LIMIT_SIZE = 500;

	private final NotificationDispatchConfigJpaRepository notificationConfigJpaRepository;

	public NotificationInfoRepositoryAdapter(NotificationDispatchConfigJpaRepository notificationConfigJpaRepository) {
		this.notificationConfigJpaRepository = notificationConfigJpaRepository;
	}

	@Override
	public NotificationInfo retrieveNotificationInfosByTopic(String topic, int page) {
		PageRequest pageRequest = PageRequest.of(page, LIMIT_SIZE, DEFAULT_SORT);
		Slice<NotificationInfoProjection> projections = notificationConfigJpaRepository
			.findAllByEnableTrueAndTopic(NotificationTopic.findByValue(topic), pageRequest);
		return new NotificationInfo(
			projections.stream()
				.collect(Collectors.groupingBy(
					projection -> NotificationInfo.Topic.initialize(projection.topic()),
					Collectors.mapping(projection -> new NotificationInfo.Subscriber(
						projection.memberNo(),
						projection.deviceOs(),
						projection.deviceToken()
					), Collectors.toList())
				)),
			projections.isLast()
		);
	}

	@Override
	public NotificationInfo retrieveNotificationInfosByTopics(Set<NotificationTopic> topics, int page) {
		PageRequest pageRequest = PageRequest.of(page, LIMIT_SIZE, DEFAULT_SORT);
		Slice<NotificationInfoProjection> projections = notificationConfigJpaRepository
			.findAllByEnableTrueAndTopicIn(topics, pageRequest);
		return new NotificationInfo(
			projections.stream()
				.collect(Collectors.groupingBy(
					projection -> NotificationInfo.Topic.initialize(projection.topic()),
					Collectors.mapping(projection -> new NotificationInfo.Subscriber(
						projection.memberNo(),
						projection.deviceOs(),
						projection.deviceToken()
					), Collectors.toList())
				)),
			projections.isLast()
		);
	}
}
