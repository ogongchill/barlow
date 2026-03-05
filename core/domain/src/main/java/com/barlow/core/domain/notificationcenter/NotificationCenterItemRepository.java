package com.barlow.core.domain.notificationcenter;

import com.barlow.core.domain.User;

public interface NotificationCenterItemRepository {
	void deleteAllItems(User user);
}
