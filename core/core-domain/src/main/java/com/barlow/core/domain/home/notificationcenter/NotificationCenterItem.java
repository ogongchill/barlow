package com.barlow.core.domain.home.notificationcenter;

import java.time.LocalDateTime;

/**
 * @param iconImagePath todo : 2025.01.11 - 알림센터 jpa entity 에서 제거
 */
public record NotificationCenterItem(
	String billId,
	String notificationTopic,
	String iconImagePath,
	String title,
	String body,
	LocalDateTime createdAt
) {
}
