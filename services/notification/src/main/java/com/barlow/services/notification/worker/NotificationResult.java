package com.barlow.services.notification.worker;

import java.util.List;
import java.util.Map;

import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MessagingErrorCode;

public record NotificationResult(
	Map<Message, MessagingErrorCode> result
) {
	boolean hasRetryableFailure() {
		return result.entrySet()
			.stream()
			.filter(entry -> entry.getValue().equals(MessagingErrorCode.INTERNAL)
				|| entry.getValue().equals(MessagingErrorCode.UNAVAILABLE)
				|| entry.getValue().equals(MessagingErrorCode.QUOTA_EXCEEDED))
			.map(Map.Entry::getKey)
			.findAny()
			.isPresent();
	}

	List<Message> getRetryableMessages() {
		return result.entrySet()
			.stream()
			.filter(entry -> entry.getValue().equals(MessagingErrorCode.INTERNAL)
				|| entry.getValue().equals(MessagingErrorCode.UNAVAILABLE)
				|| entry.getValue().equals(MessagingErrorCode.QUOTA_EXCEEDED))
			.map(Map.Entry::getKey)
			.toList();
	}

	List<Message> getNonRetryableMessages() {
		return result.entrySet()
			.stream()
			.filter(entry -> !entry.getValue().equals(MessagingErrorCode.INTERNAL)
				&& !entry.getValue().equals(MessagingErrorCode.UNAVAILABLE)
				&& !entry.getValue().equals(MessagingErrorCode.QUOTA_EXCEEDED))
			.map(Map.Entry::getKey)
			.toList();
	}
}
