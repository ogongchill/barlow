package com.barlow.core.api.controller.v1.home;

import java.util.List;

import com.barlow.core.support.response.Constant;

public record HomeResponse(
	boolean isNotificationArrived,
	SubscribeSection subscribeSection
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
}
