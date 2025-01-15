package com.barlow.notification;

import java.util.List;

public record NotificationCenterItemInfo(
	Long memberNo,
	String topic,
	List<BillItemInfo> items
) {
	static NotificationCenterItemInfo of(Long memberNo, String topic, List<NotificationRequest.BillInfo> billInfos) {
		List<BillItemInfo> billItemInfos = billInfos.stream()
			.map(info -> new BillItemInfo(info.billId(), info.billName()))
			.toList();
		return new NotificationCenterItemInfo(memberNo, topic, billItemInfos);
	}

	public record BillItemInfo(String billId, String billName) {
	}
}
