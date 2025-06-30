package com.barlow.services.notification.worker;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.firebase.messaging.MessagingErrorCode;

public record NotificationResult(
	Map<NotificationMessage, MessagingErrorCode> failureResult,
	List<NotificationMessage> successResult
) {
	boolean hasRetryableFailure() {
		return failureResult.entrySet().stream()
			.filter(entry
				-> entry.getValue().equals(MessagingErrorCode.INTERNAL)
				|| entry.getValue().equals(MessagingErrorCode.UNAVAILABLE)
				|| entry.getValue().equals(MessagingErrorCode.QUOTA_EXCEEDED))
			.map(Map.Entry::getKey)
			.findAny()
			.isPresent();
	}

	public List<NotificationMessage> getRetryableMessages() {
		return failureResult.entrySet().stream()
			.filter(entry
				-> entry.getValue().equals(MessagingErrorCode.INTERNAL)
				|| entry.getValue().equals(MessagingErrorCode.UNAVAILABLE)
				|| entry.getValue().equals(MessagingErrorCode.QUOTA_EXCEEDED))
			.map(Map.Entry::getKey)
			.toList();
	}

	List<NotificationMessage> getNonRetryableMessages() {
		return failureResult.entrySet().stream()
			.filter(entry
				-> !entry.getValue().equals(MessagingErrorCode.INTERNAL)
				&& !entry.getValue().equals(MessagingErrorCode.UNAVAILABLE)
				&& !entry.getValue().equals(MessagingErrorCode.QUOTA_EXCEEDED))
			.map(Map.Entry::getKey)
			.toList();
	}

	public Map<MessagingErrorCode, List<NotificationMessage>> groupRetryableFailuresByError() {
		return failureResult.entrySet().stream()
			.filter(entry
				-> entry.getValue().equals(MessagingErrorCode.INTERNAL)
				|| entry.getValue().equals(MessagingErrorCode.UNAVAILABLE)
				|| entry.getValue().equals(MessagingErrorCode.QUOTA_EXCEEDED))
			.collect(Collectors.groupingBy(
				Map.Entry::getValue,
				Collectors.flatMapping(
					entry -> Stream.of(entry.getKey()),
					Collectors.toList()
				)
			));
	}

	public List<NotificationMessage> getSuccessMessages() {
		return successResult;
	}
}
