package com.barlow.core.domain.home;

import org.springframework.stereotype.Repository;

import com.barlow.core.domain.User;

@Repository
public interface MyNotificationCenterRepository {
	boolean existsTodayNotification(User user);
}
