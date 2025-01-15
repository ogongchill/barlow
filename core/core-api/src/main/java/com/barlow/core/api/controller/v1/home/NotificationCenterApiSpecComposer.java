package com.barlow.core.api.controller.v1.home;

import java.util.List;

import com.barlow.core.domain.home.notificationcenter.NotificationCenterItem;

public class NotificationCenterApiSpecComposer {

	private final List<NotificationCenterItem> items;

	public NotificationCenterApiSpecComposer(List<NotificationCenterItem> items) {
		this.items = items;
	}

	NotificationCenterResponse compose(String filterTopic) {
		if (filterTopic == null || filterTopic.isEmpty()) {
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
