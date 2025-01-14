package com.barlow.core.domain.home.notificationcenter;

import java.time.LocalDateTime;

public record NotificationCenterItem(
	String billId,
	String notificationTopic,
	String iconImagePath,
	String title,
	String body,
	LocalDateTime createdAt
) {
}
