package com.barlow.services.notification.worker;

import org.springframework.stereotype.Component;

import com.barlow.core.enumerate.DeviceOs;
import com.google.firebase.messaging.FirebaseMessaging;

@Component
public class AosNotificationSender extends NotificationSender {

	protected AosNotificationSender(FirebaseMessaging firebaseMessaging) {
		super(firebaseMessaging);
	}

	@Override
	DeviceOs supportedOs() {
		return DeviceOs.ANDROID;
	}
}
