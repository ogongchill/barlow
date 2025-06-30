package com.barlow.services.notification.worker;

import com.barlow.core.enumerate.DeviceOs;
import com.google.firebase.messaging.Message;

public interface NotificationMessage {

	String id();

	String token();

	DeviceOs os();

	long memberNo();

	Message message();

	MessageStatus status();

	NotificationMessage change(MessageStatus status);
}
