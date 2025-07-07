package com.barlow.services.notification.worker.ios;

import com.barlow.core.enumerate.DeviceOs;
import com.barlow.services.notification.worker.MessageStatus;
import com.barlow.services.notification.worker.NotificationMessage;
import com.google.firebase.messaging.Message;

public record IosMessage(
	String id,
	String token,
	DeviceOs os,
	long memberNo,
	Message message,
	MessageStatus status
) implements NotificationMessage {
	@Override
	public NotificationMessage change(MessageStatus status) {
		return new IosMessage(id, token, os, memberNo, message, status);
	}
}
