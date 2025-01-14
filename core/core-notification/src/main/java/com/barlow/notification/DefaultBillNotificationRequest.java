package com.barlow.notification;

import java.util.List;

import org.jetbrains.annotations.NotNull;

/**
 * 법안 알림 : 법안 알림에 관한 기본 전송 알림에 사용한다
 * @param topic 접수의안, 본회의부의안건, 본회의의결, 재의요구, 공포
 * @param representationBill [topic]을 대표하는 하나의 법안 이름
 * @param billInfos [topic] 조건에 해당하는 법안의 정보들 : {법안 ID, 법안 이름}
 * @param totalCount [topic] 조건에 해당하는 법안의 총 개수
 * @see NotificationType
 */
public record DefaultBillNotificationRequest(
	@NotNull NotificationType type,
	@NotNull String topic,
	@NotNull String representationBill,
	@NotNull List<BillInfo> billInfos,
	@NotNull Integer totalCount
) implements NotificationRequest {

	public static DefaultBillNotificationRequest of(String topic, List<BillInfo> billInfos) {
		if (topic == null) {
			throw new IllegalArgumentException("topic 은 null 이 될 수 없습니다");
		}
		return new DefaultBillNotificationRequest(
			NotificationType.DEFAULT,
			topic,
			billInfos.getFirst().billName(),
			billInfos,
			billInfos.size()
		);
	}

	@Override
	public List<BillInfo> billInfosByTopic(String topic) {
		if (!this.topic.equals(topic)) {
			throw new IllegalArgumentException("잘못된 topic 입니다");
		}
		return billInfos;
	}
}
