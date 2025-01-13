package com.barlow.core.domain.home;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.barlow.core.domain.User;
import com.barlow.core.domain.home.notificationcenter.NotificationCenterItem;

@Repository
public interface MyNotificationCenterRepository {

	boolean existsTodayNotification(User user);

	List<NotificationCenterItem> retrieveNotificationItems(User user);
}
