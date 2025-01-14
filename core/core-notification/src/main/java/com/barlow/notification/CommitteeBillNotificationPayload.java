package com.barlow.notification;

import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;

/**
 * 법안 알림 : 소관위접수 법안 알림에 사용한다
 * @param topicsWithBillInfos {key:소관위원회명 value:[key]에 해당하는 {법안 ID, 법안 이름} 리스트}
 * @see NotificationType
 */
public record CommitteeBillNotificationPayload(
	@NotNull NotificationType type,
	@NotNull Map<String, List<BillInfo>> topicsWithBillInfos
) implements NotificationPayload {

	public static CommitteeBillNotificationPayload from(Map<String, List<BillInfo>> topicsWithBillInfos) {
		if (topicsWithBillInfos == null) {
			throw new IllegalArgumentException("null 파라미터는 들어올 수 없습니다");
		}
		return new CommitteeBillNotificationPayload(NotificationType.STANDING_COMMITTEE, topicsWithBillInfos);
	}

	@Override
	public List<BillInfo> billInfosByTopic(String topic) {
		return topicsWithBillInfos.get(topic);
	}
}
