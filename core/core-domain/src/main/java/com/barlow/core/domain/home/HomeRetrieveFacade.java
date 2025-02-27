package com.barlow.core.domain.home;

import java.util.List;

import org.springframework.stereotype.Service;

import com.barlow.core.domain.User;
import com.barlow.core.domain.home.notificationcenter.NotificationCenterItem;
import com.barlow.core.domain.home.notificationcenter.NotificationCenterItemRetrieveService;

@Service
public class HomeRetrieveFacade {

	private final HomeRetrieveService homeRetrieveService;
	private final NotificationCenterItemRetrieveService notificationCenterItemRetrieveService;

	public HomeRetrieveFacade(
		HomeRetrieveService homeRetrieveService,
		NotificationCenterItemRetrieveService notificationCenterItemRetrieveService
	) {
		this.homeRetrieveService = homeRetrieveService;
		this.notificationCenterItemRetrieveService = notificationCenterItemRetrieveService;
	}

	public HomeStatus retrieveHome(User user) {
		return homeRetrieveService.retrieveHome(user);
	}

	public List<NotificationCenterItem> retrieveNotificationCenter(User user) {
		return notificationCenterItemRetrieveService.retrieveNotificationCenterItems(user);
	}
}
