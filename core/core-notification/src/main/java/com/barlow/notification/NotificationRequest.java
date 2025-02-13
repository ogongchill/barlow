package com.barlow.notification;

import java.util.List;

public interface NotificationRequest {

	NotificationType type();

	List<BillInfo> billInfosByTopic(String topic);

	record BillInfo(String billId, String billName) {
	}
}
