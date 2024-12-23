package com.barlow.notification;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Repository;

@Repository
public interface NotificationInfoRepository {

	List<NotificationInfo> retrieveNotificationInfosByTopic(String topic);

	List<NotificationInfo> retrieveNotificationInfosByTopics(Set<String> topics);
}
