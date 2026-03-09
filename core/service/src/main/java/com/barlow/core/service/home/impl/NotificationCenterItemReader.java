package com.barlow.core.service.home.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.barlow.core.domain.User;
import com.barlow.core.domain.notificationcenter.NotificationCenterItem;
import com.barlow.core.domain.notificationcenter.MyNotificationCenterRepository;

@Component
public class NotificationCenterItemReader {

	private final MyNotificationCenterRepository myNotificationCenterRepository;

	public NotificationCenterItemReader(MyNotificationCenterRepository myNotificationCenterRepository) {
		this.myNotificationCenterRepository = myNotificationCenterRepository;
	}

	public List<NotificationCenterItem> readNotificationItems(User user) {
		return myNotificationCenterRepository.retrieveNotificationItems(user);
	}
}
