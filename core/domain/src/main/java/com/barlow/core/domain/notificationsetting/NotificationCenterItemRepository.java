package com.barlow.core.domain.notificationsetting;

import org.springframework.stereotype.Repository;

import com.barlow.core.domain.User;

@Repository
public interface NotificationCenterItemRepository {
	void deleteAllItems(User user);
}
