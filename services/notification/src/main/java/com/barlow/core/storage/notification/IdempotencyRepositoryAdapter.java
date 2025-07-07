package com.barlow.core.storage.notification;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.barlow.services.notification.worker.IdempotencyRepository;
import com.barlow.services.notification.worker.MessageStatus;
import com.barlow.services.notification.worker.NotificationMessage;

@Component
public class IdempotencyRepositoryAdapter implements IdempotencyRepository {

	private static final Map<String, NotificationMessage> STORAGE = new ConcurrentHashMap<>();

	@Override
	public boolean exists(String id) {
		return STORAGE.get(id) != null;
	}

	@Override
	public void markAsPending(NotificationMessage message) {
		STORAGE.computeIfAbsent(message.id(), id -> message.change(MessageStatus.PENDING));
	}

	@Override
	public void markAsFailed(NotificationMessage message) {
		STORAGE.computeIfAbsent(message.id(), id -> message.change(MessageStatus.FAILED));
	}

	@Override
	public void markAsSuccess(NotificationMessage message) {
		STORAGE.computeIfAbsent(message.id(), id -> message.change(MessageStatus.SUCCEEDED));
	}

	@Override
	public boolean checkFailed(String id) {
		return STORAGE.get(id).status().equals(MessageStatus.FAILED);
	}
}
