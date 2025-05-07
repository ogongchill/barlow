package com.barlow.services.notification.worker;

import org.springframework.stereotype.Component;

import com.google.firebase.messaging.FirebaseMessaging;

@Component
public class IOSNotificationSender extends NotificationSender {

	public IOSNotificationSender(FirebaseMessaging firebaseMessaging) {
		super(firebaseMessaging);
	}
}
