package com.barlow.notification;

import java.util.List;

import com.barlow.core.enumerate.NotificationTopic;

public interface NotificationRequest {

	NotificationType type();

	List<BillInfo> billInfosByTopic(NotificationTopic topic);

	record BillInfo(String billId, String billName) {
	}
}
