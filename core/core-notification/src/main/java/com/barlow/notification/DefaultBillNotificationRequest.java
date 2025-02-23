package com.barlow.notification;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jetbrains.annotations.NotNull;

/**
 * 법안 알림 : 법안 알림에 관한 기본 전송 알림에 사용한다
 * @param topicsWithBillInfos {key:법안의 ProgressStatus value:[key]에 해당하는 BillSummary(법안요약정보)}
 * @see BillSummary
 * @see NotificationType
 */
public record DefaultBillNotificationRequest(
	@NotNull NotificationType type,
	@NotNull Map<String, BillSummary> topicsWithBillInfos
) implements NotificationRequest {

	public static DefaultBillNotificationRequest from(Map<String, List<BillInfo>> topicsWithBillInfos) {
		if (topicsWithBillInfos == null) {
			throw new IllegalArgumentException("null 파라미터는 들어올 수 없습니다");
		}
		return new DefaultBillNotificationRequest(
			NotificationType.DEFAULT,
			topicsWithBillInfos.entrySet().stream()
				.collect(Collectors.toMap(
					Map.Entry::getKey,
					entry -> DefaultBillNotificationRequest.BillSummary.from(entry.getValue())
				))
		);
	}

	@Override
	public List<BillInfo> billInfosByTopic(String topic) {
		return topicsWithBillInfos.get(topic).billInfos;
	}

	/**
	 * 법안 요약 정보
	 * @param billInfos [topic] 조건에 해당하는 법안의 정보들 : {법안 ID, 법안 이름}
	 * @param representationBill [topic]을 대표하는 하나의 법안 이름
	 * @param totalCount [topic] 조건에 해당하는 법안의 총 개수
	 */
	record BillSummary(
		@NotNull List<BillInfo> billInfos,
		@NotNull String representationBill,
		@NotNull Integer totalCount
	) {
		static DefaultBillNotificationRequest.BillSummary from(List<BillInfo> billInfos) {
			return new DefaultBillNotificationRequest.BillSummary(
				billInfos,
				billInfos.getFirst().billName(),
				billInfos.size()
			);
		}
	}
}
