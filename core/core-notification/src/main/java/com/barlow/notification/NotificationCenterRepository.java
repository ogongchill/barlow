package com.barlow.notification;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.barlow.notification.worker.NotificationCenterInfo;

@Repository
public interface NotificationCenterRepository {
	void registerAll(List<NotificationCenterInfo> notificationCenterInfos);
}
