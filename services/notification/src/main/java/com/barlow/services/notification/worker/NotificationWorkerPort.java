package com.barlow.services.notification.worker;

import com.barlow.services.notification.MessageTemplate;
import com.barlow.services.notification.NotificationInfo;

public interface NotificationWorkerPort {
	void notify(MessageTemplate messageTemplate, NotificationInfo notificationInfo);
}
