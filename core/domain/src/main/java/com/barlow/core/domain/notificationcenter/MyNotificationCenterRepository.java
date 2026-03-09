package com.barlow.core.domain.notificationcenter;

import java.util.List;

import com.barlow.core.domain.User;

public interface MyNotificationCenterRepository {

	boolean existsTodayNotification(User user);

	List<NotificationCenterItem> retrieveNotificationItems(User user);
}
