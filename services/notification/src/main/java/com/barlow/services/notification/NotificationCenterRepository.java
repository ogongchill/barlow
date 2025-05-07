package com.barlow.services.notification;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface NotificationCenterRepository {
	void registerAll(List<NotificationCenterItemInfo> notificationCenterItemInfos);
}
