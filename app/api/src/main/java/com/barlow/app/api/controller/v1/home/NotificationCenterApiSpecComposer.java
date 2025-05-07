package com.barlow.app.api.controller.v1.home;

import java.util.List;

import com.barlow.core.domain.home.notificationcenter.NotificationCenterItem;
import com.barlow.core.enumerate.NotificationTopic;

public class NotificationCenterApiSpecComposer {

	private final List<NotificationCenterItem> items;

	public NotificationCenterApiSpecComposer(List<NotificationCenterItem> items) {
		this.items = items;
	}

	NotificationCenterResponse compose(NotificationTopic filterTopic) {
		if (filterTopic == null) {
			return new NotificationCenterResponse(
				items.stream()
					.map(NotificationCenterResponse.Item::from)
					.toList()
			);
		}
		return new NotificationCenterResponse(
			items.stream()
				.filter(item -> item.notificationTopic().equals(filterTopic))
				.map(NotificationCenterResponse.Item::from)
				.toList()
		);
	}
}
