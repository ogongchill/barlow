package com.barlow.services.notification.worker.ios;

import org.springframework.stereotype.Component;

import com.barlow.core.enumerate.DeviceOs;
import com.barlow.services.notification.worker.NotificationSender;
import com.google.firebase.messaging.FirebaseMessaging;

@Component
public class IosNotificationSender extends NotificationSender {

	protected IosNotificationSender(FirebaseMessaging firebaseMessaging) {
		super(firebaseMessaging);
	}

	@Override
	public DeviceOs supportedOs() {
		return DeviceOs.IOS;
	}
}
