package com.barlow.services.notification;

import org.springframework.stereotype.Component;

@Component
public interface NotificationSendPort {
	void sendCall(NotificationRequest request);
}
