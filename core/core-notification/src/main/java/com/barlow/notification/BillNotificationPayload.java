package com.barlow.notification;

import java.util.Collections;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record BillNotificationPayload(
	@NotNull NotificationType type,
	@Nullable String topic,
	@Nullable String representationBill,
	@Nullable Integer totalCount,
	@NotNull Map<String, Integer> topicsWithCount
) implements NotificationPayload {
	/**
	 * 법안 알림 : 법안 알림에 관한 기본 전송 알림에 사용한다
	 * @param type 기본입법알림
	 * @param topic 접수의안, 본회의부의안건, 본회의의결, 재의요구, 공포
	 * @param representationBill [topic] 을 대표하는 하나의 법안 이름
	 * @param totalCount [topic]에 조건에 해당하는 법안의 총 개수
	 * @see NotificationType
	 */
	public static BillNotificationPayload defaultOf(
		String type,
		String topic,
		String representationBill,
		Integer totalCount
	) {
		if (type == null || topic == null || representationBill == null || totalCount == null) {
			throw new IllegalArgumentException("null 파라미터는 들어올 수 없습니다");
		}
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
	public static BillNotificationPayload standingCommitteeOf(String type, Map<String, Integer> topicsWithCount) {
		if (type == null || topicsWithCount == null) {
			throw new IllegalArgumentException("null 파라미터는 들어올 수 없습니다");
		}
		return new BillNotificationPayload(
			NotificationType.valueOf(type),
			null,
			null,
			null,
			topicsWithCount
		);
	}
}
