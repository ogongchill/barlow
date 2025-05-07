package com.barlow.app.api.controller.v1.home;

import java.time.LocalDateTime;
import java.util.List;

import com.barlow.core.domain.home.notificationcenter.NotificationCenterItem;
import com.barlow.app.support.response.Constant;

public record NotificationCenterResponse(
	List<Item> items
) {
	record Item(
		String topic,
		String title,
		String body,
		String iconUrl,
		String billId,
		LocalDateTime createdAt
	) {
		static Item from(NotificationCenterItem item) {
			return new Item(
				item.notificationTopic().getValue(),
				item.title(),
				item.body(),
				Constant.IMAGE_ACCESS_URL + item.notificationTopic().getIconPath(),
				item.billId(),
				item.createdAt()
			);
		}
	}
}
