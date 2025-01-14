package com.barlow.notification;

public interface NotificationPayload {
	NotificationType type();

	record BillInfo(
		String billId,
		String billName
	) {
	}
}
