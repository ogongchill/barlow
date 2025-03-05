package com.barlow.core.api.controller.v1.home;

import java.time.LocalDate;
import java.util.List;

import com.barlow.core.domain.home.MyHomeStatus;
import com.barlow.core.domain.home.todaybill.TodayBillPostThumbnail;

public class HomeResponseApiSpecComposer {

	private final MyHomeStatus myHomeStatus;
	private final List<TodayBillPostThumbnail> postThumbnails;

	public HomeResponseApiSpecComposer(MyHomeStatus myHomeStatus, List<TodayBillPostThumbnail> postThumbnails) {
		this.myHomeStatus = myHomeStatus;
		this.postThumbnails = postThumbnails;
	}

	public HomeResponse compose(LocalDate today) {
		List<HomeResponse.SubscribeLegislationBody> subscribeLegislationBodies = myHomeStatus
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
			myHomeStatus.isNotificationArrived(),
			HomeResponse.SubscribeSection.from(subscribeLegislationBodies),
			HomeResponse.TodayBillPostSection.of(today, billPostThumbnails)
		);
	}
}
