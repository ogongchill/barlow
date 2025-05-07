package com.barlow.services.notification.worker;

import static com.barlow.services.notification.NotificationInfo.Subscriber;

import com.google.firebase.messaging.Message;

public interface MessageProvider {
	Message provide(String messageTitle, String messageBody, Subscriber subscriber);
}
