package com.barlow.services.notification.worker.android;

import org.springframework.stereotype.Component;

import com.barlow.core.enumerate.DeviceOs;
import com.barlow.services.notification.worker.NotificationSender;
import com.google.firebase.messaging.FirebaseMessaging;

@Component
public class AosNotificationSender extends NotificationSender {

	protected AosNotificationSender(FirebaseMessaging firebaseMessaging) {
		super(firebaseMessaging);
	}

	@Override
	public DeviceOs supportedOs() {
		return DeviceOs.ANDROID;
	}
}
