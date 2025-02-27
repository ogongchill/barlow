package com.barlow.core.domain.home.notificationcenter;

import java.time.LocalDateTime;

import com.barlow.core.enumerate.NotificationTopic;

public record NotificationCenterItem(
	String billId,
	NotificationTopic notificationTopic,
	String title,
	String body,
	LocalDateTime createdAt
) {
}
