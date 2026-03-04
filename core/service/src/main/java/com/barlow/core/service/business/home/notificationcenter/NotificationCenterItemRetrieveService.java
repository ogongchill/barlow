package com.barlow.core.service.business.home.notificationcenter;

import java.util.List;

import org.springframework.stereotype.Service;

import com.barlow.core.domain.User;
import com.barlow.core.domain.home.notificationcenter.NotificationCenterItem;
import com.barlow.core.service.implement.home.notificationcenter.NotificationCenterItemReader;

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
