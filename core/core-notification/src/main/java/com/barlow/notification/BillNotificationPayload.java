package com.barlow.notification;

import java.util.Collections;
import java.util.Map;

public record BillNotificationPayload(
	NotificationType type,
	String topic,
	String representationBill,
	int totalCount,
	Map<String, Integer> topicsWithCount
) implements NotificationPayload {
	/**
	 * 법안 알림 : 법안 알림에 관한 기본 전송 알림에 사용한다
	 * @param type 기본입법알림
	 * @param topic 접수의안, 본회의부의안건, 본회의의결, 재의요구, 공포
	 * @param representationBill [topic] 을 대표하는 하나의 법안 이름
	 * @param totalCount [topic]에 조건에 해당하는 법안의 총 개수
	 * @see NotificationType
	 */
	public static BillNotificationPayload of(String type, String topic, String representationBill, int totalCount) {
		return new BillNotificationPayload(
			NotificationType.valueOf(type),
			topic,
			representationBill,
			totalCount,
			Collections.emptyMap()
		);
	}

	/**
	 * 법안 알림 : 소관위접수 법안 알림에 사용한다
	 * @param type 소관위원회알림
	 * @param topicsWithCount {key:소관위원회명 value:입법갯수}
	 * @see NotificationType
	 */
	public static BillNotificationPayload of(String type, Map<String, Integer> topicsWithCount) {
		return new BillNotificationPayload(
			NotificationType.valueOf(type),
			null,
			null,
			0,
			topicsWithCount
		);
	}
}
