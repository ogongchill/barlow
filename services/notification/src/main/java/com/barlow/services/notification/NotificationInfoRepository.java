package com.barlow.services.notification;

import java.util.Set;

import org.springframework.stereotype.Repository;

import com.barlow.core.enumerate.NotificationTopic;

@Repository
public interface NotificationInfoRepository {

	NotificationInfo retrieveNotificationInfosByTopic(String topic);

	NotificationInfo retrieveNotificationInfosByTopics(Set<NotificationTopic> topics);
}
