package com.barlow.notification;

import org.springframework.stereotype.Component;

@Component
public interface NotificationSendPort {
	void sendCall(NotificationPayload payload);
}
