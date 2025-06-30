package com.barlow.services.notification.worker;

public interface NotificationRetryPort {
	void retry(RetryMessage retryMessage);
}
