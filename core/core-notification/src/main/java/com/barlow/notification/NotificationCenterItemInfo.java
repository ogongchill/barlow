package com.barlow.notification;

import java.util.List;

import com.barlow.core.enumerate.NotificationTopic;

public record NotificationCenterItemInfo(
	Long memberNo,
	NotificationTopic topic,
	List<BillItemInfo> items
) {
	static NotificationCenterItemInfo of(
		Long memberNo,
		NotificationTopic topic,
		List<NotificationRequest.BillInfo> billInfos
	) {
		List<BillItemInfo> billItemInfos = billInfos.stream()
			.map(info -> new BillItemInfo(info.billId(), info.billName()))
			.toList();
		return new NotificationCenterItemInfo(memberNo, topic, billItemInfos);
	}

	public record BillItemInfo(String billId, String billName) {
	}
}
