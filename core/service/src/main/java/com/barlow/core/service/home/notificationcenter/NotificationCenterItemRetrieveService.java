package com.barlow.core.service.home.notificationcenter;

import java.util.List;

import org.springframework.stereotype.Service;

import com.barlow.core.domain.User;
import com.barlow.core.domain.home.notificationcenter.NotificationCenterItem;

@Service
public class NotificationCenterItemRetrieveService {

	private final NotificationCenterItemReader notificationCenterItemReader;

	public NotificationCenterItemRetrieveService(NotificationCenterItemReader notificationCenterItemReader) {
		this.notificationCenterItemReader = notificationCenterItemReader;
	}

	public List<NotificationCenterItem> retrieveNotificationCenterItems(User user) {
		return notificationCenterItemReader.readNotificationItems(user);
	}
}
