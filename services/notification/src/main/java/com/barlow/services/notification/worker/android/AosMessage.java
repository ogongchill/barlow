package com.barlow.services.notification.worker.android;

import com.barlow.core.enumerate.DeviceOs;
import com.barlow.services.notification.worker.MessageStatus;
import com.barlow.services.notification.worker.NotificationMessage;
import com.google.firebase.messaging.Message;

public record AosMessage(
	String id,
	String token,
	DeviceOs os,
	long memberNo,
	Message message,
	MessageStatus status
) implements NotificationMessage {
	@Override
	public NotificationMessage change(MessageStatus status) {
		return new AosMessage(id, token, os, memberNo, message, status);
	}
}
