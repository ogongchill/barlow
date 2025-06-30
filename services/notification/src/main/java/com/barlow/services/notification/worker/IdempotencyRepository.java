package com.barlow.services.notification.worker;

public interface IdempotencyRepository {

	boolean exists(String id);

	void markAsPending(NotificationMessage message);

	void markAsFailed(NotificationMessage message);

	void markAsSuccess(NotificationMessage message);

	boolean checkFailed(String id);
}
