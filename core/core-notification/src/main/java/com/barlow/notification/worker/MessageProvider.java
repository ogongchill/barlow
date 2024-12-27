package com.barlow.notification.worker;

import static com.barlow.notification.NotificationInfo.Subscriber;

import com.google.firebase.messaging.Message;

public interface MessageProvider {
	Message provide(String messageTitle, String messageBody, Subscriber subscriber);
}
