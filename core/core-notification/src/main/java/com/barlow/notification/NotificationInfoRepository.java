package com.barlow.notification;

import java.util.Set;

import org.springframework.stereotype.Repository;

@Repository
public interface NotificationInfoRepository {

	NotificationInfo retrieveNotificationInfosByTopic(String topic);

	NotificationInfo retrieveNotificationInfosByTopics(Set<String> topics);
}
