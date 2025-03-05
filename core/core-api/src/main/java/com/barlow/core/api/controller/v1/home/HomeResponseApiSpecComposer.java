package com.barlow.core.api.controller.v1.home;

import java.time.LocalDate;
import java.util.List;

import com.barlow.core.domain.home.HomeStatus;
import com.barlow.core.domain.home.todaybill.TodayBillPostThumbnail;

public class HomeResponseApiSpecComposer {

	private final HomeStatus homeStatus;
	private final List<TodayBillPostThumbnail> postThumbnails;

	public HomeResponseApiSpecComposer(HomeStatus homeStatus, List<TodayBillPostThumbnail> postThumbnails) {
		this.homeStatus = homeStatus;
		this.postThumbnails = postThumbnails;
	}

	public HomeResponse compose(LocalDate today) {
		List<HomeResponse.SubscribeLegislationBody> subscribeLegislationBodies = homeStatus
			.myLegislationAccounts()
			.stream()
			.map(status -> new HomeResponse.SubscribeLegislationBody(
				status.getNo(),
				status.getBodyType(),
				status.getIconImagePath()
			))
			.toList();
		List<HomeResponse.BillPostThumbnail> billPostThumbnails = postThumbnails.stream()
			.map(thumbnail -> new HomeResponse.BillPostThumbnail(
				thumbnail.billId(),
				thumbnail.billName(),
				thumbnail.proposers(),
				thumbnail.createdAt()
			))
			.toList();
		return new HomeResponse(
			homeStatus.isNotificationArrived(),
			HomeResponse.SubscribeSection.from(subscribeLegislationBodies),
			HomeResponse.TodayBillPostSection.of(today, billPostThumbnails)
		);
	}
}
