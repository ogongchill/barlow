package com.barlow.core.domain.home.notificationcenter;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.barlow.core.domain.User;

@Repository
public interface MyNotificationCenterRepository {

	boolean existsTodayNotification(User user);

	List<NotificationCenterItem> retrieveNotificationItems(User user);
}
