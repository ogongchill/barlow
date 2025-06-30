package com.barlow.services.notification.worker;

import java.util.List;
import java.util.Map;

import com.barlow.core.enumerate.DeviceOs;
import com.google.firebase.messaging.MessagingErrorCode;

public record RetryMessage(
	DeviceOs os,
	Map<MessagingErrorCode, List<NotificationMessage>> retryableMessages
) {
}
