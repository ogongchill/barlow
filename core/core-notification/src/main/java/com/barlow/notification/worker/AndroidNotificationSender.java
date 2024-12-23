package com.barlow.notification.worker;

import org.springframework.stereotype.Component;

import com.google.firebase.messaging.FirebaseMessaging;

@Component
public class AndroidNotificationSender extends NotificationSender {

	protected AndroidNotificationSender(FirebaseMessaging firebaseMessaging) {
		super(firebaseMessaging);
	}
}
