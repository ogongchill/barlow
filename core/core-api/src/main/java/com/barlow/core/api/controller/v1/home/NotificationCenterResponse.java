package com.barlow.core.api.controller.v1.home;

import java.time.LocalDateTime;
import java.util.List;

import com.barlow.core.domain.home.notificationcenter.NotificationCenterItem;
import com.barlow.core.support.response.Constant;

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
				item.notificationTopic(),
				item.title(),
				item.body(),
				Constant.IMAGE_ACCESS_URL + item.iconImagePath(),
				item.billId(),
				item.createdAt()
			);
		}
	}
}
