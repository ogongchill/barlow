package com.barlow.notification;

import java.util.Map;

import org.jetbrains.annotations.NotNull;

public record CommitteeBillNotificationPayload(
	@NotNull NotificationType type,
	@NotNull Map<String, Integer> topicsWithCount
) implements NotificationPayload {
	/**
	 * 법안 알림 : 소관위접수 법안 알림에 사용한다
	 * @param topicsWithCount {key:소관위원회명 value:입법갯수}
	 * @see NotificationType
	 */
	public static CommitteeBillNotificationPayload from(Map<String, Integer> topicsWithCount) {
		if (topicsWithCount == null) {
			throw new IllegalArgumentException("null 파라미터는 들어올 수 없습니다");
		}
		return new CommitteeBillNotificationPayload(NotificationType.STANDING_COMMITTEE, topicsWithCount);
	}
}
