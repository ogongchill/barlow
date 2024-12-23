package com.barlow.notification;

import java.util.List;

public interface NotificationInfoReader {
	List<NotificationInfo> readNotificationInfos(BillNotificationPayload payload);
}
