package com.barlow.core.api.controller.v1.home;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import com.barlow.core.support.response.Constant;

public record HomeResponse(
	boolean isNotificationArrived,
	SubscribeSection subscribeSection,
	TodayBillPostSection todayBillPostSection
) {

	record SubscribeSection(
		String display,
		List<SubscribeLegislationBody> subscribeLegislationBodies
	) {
		static SubscribeSection from(List<SubscribeLegislationBody> subscribeLegislationBodies) {
			if (subscribeLegislationBodies.isEmpty()) {
				return new SubscribeSection(
					"아직 구독한 상임위원회가 없어요\n구독하고 입법 정보를 받아보세요",
					subscribeLegislationBodies
				);
			}
			return new SubscribeSection(null, subscribeLegislationBodies);
		}
	}

	static class SubscribeLegislationBody {

		private final long no;
		private final String bodyType;
		private final String iconImageUrl;

		public SubscribeLegislationBody(long no, String bodyType, String iconImagePath) {
			this.no = no;
			this.bodyType = bodyType;
			this.iconImageUrl = Constant.IMAGE_ACCESS_URL + iconImagePath;
		}

		public long getNo() {
			return no;
		}

		public String getBodyType() {
			return bodyType;
		}

		public String getIconImageUrl() {
			return iconImageUrl;
		}
	}

	record TodayBillPostSection(
		String display,
		List<BillPostThumbnail> postThumbnails
	) {
		static TodayBillPostSection of(LocalDate today, List<BillPostThumbnail> postThumbnails) {
			DayOfWeek dayOfWeek = today.getDayOfWeek();
			if (dayOfWeek.equals(DayOfWeek.SATURDAY) || dayOfWeek.equals(DayOfWeek.SUNDAY)) {
				return new TodayBillPostSection(
					"주말에는 새로운 법안이 발의되지 않아요",
					postThumbnails
				);
			} else if (postThumbnails.isEmpty()) {
				return new TodayBillPostSection(
					"오늘 접수된 법안들이 아직 등록되지 않았어요",
					postThumbnails
				);
			} else {
				return new TodayBillPostSection(null, postThumbnails);
			}
		}
	}

	record BillPostThumbnail(
		String billId,
		String billName,
		String proposers,
		LocalDate createdAt
	) {
	}
}
