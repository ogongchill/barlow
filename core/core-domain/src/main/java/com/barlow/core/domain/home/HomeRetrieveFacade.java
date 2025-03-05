package com.barlow.core.domain.home;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.barlow.core.domain.User;
import com.barlow.core.domain.home.notificationcenter.NotificationCenterItem;
import com.barlow.core.domain.home.notificationcenter.NotificationCenterItemRetrieveService;
import com.barlow.core.domain.home.todaybill.TodayBillPostThumbnail;
import com.barlow.core.domain.home.todaybill.TodayBillPostThumbnailRetrieveService;

@Service
public class HomeRetrieveFacade {

	private final MyHomeInfoRetrieveService myHomeInfoRetrieveService;
	private final TodayBillPostThumbnailRetrieveService todayBillPostThumbnailRetrieveService;
	private final NotificationCenterItemRetrieveService notificationCenterItemRetrieveService;

	public HomeRetrieveFacade(
		MyHomeInfoRetrieveService myHomeInfoRetrieveService,
		TodayBillPostThumbnailRetrieveService todayBillPostThumbnailRetrieveService,
		NotificationCenterItemRetrieveService notificationCenterItemRetrieveService
	) {
		this.myHomeInfoRetrieveService = myHomeInfoRetrieveService;
		this.todayBillPostThumbnailRetrieveService = todayBillPostThumbnailRetrieveService;
		this.notificationCenterItemRetrieveService = notificationCenterItemRetrieveService;
	}

	public MyHomeStatus retrieveHome(User user) {
		return myHomeInfoRetrieveService.retrieveHome(user);
	}

	public List<TodayBillPostThumbnail> retrieveTodayBillPostThumbnail(LocalDate today) {
		return todayBillPostThumbnailRetrieveService.retrieveTodayAll(today);
	}

	public List<NotificationCenterItem> retrieveNotificationCenter(User user) {
		return notificationCenterItemRetrieveService.retrieveNotificationCenterItems(user);
	}
}
