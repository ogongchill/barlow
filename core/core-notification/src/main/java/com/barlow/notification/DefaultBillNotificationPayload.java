package com.barlow.notification;

import org.jetbrains.annotations.NotNull;

public record DefaultBillNotificationPayload(
	@NotNull NotificationType type,
	@NotNull String topic,
	@NotNull String representationBill,
	@NotNull Integer totalCount
) implements NotificationPayload {
	/**
	 * 법안 알림 : 법안 알림에 관한 기본 전송 알림에 사용한다
	 * @param topic 접수의안, 본회의부의안건, 본회의의결, 재의요구, 공포
	 * @param representationBill [topic] 을 대표하는 하나의 법안 이름
	 * @param totalCount [topic]에 조건에 해당하는 법안의 총 개수
	 * @see NotificationType
	 */
	public static DefaultBillNotificationPayload of(
		String topic,
		String representationBill,
		Integer totalCount
	) {
		if (topic == null || representationBill == null || totalCount == null) {
			throw new IllegalArgumentException("null 파라미터는 들어올 수 없습니다");
		}
		return new DefaultBillNotificationPayload(
			NotificationType.DEFAULT,
			topic,
			representationBill,
			totalCount
		);
	}
}
