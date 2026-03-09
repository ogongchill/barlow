package com.barlow.infra.notification.worker;

import static com.barlow.infra.notification.NotificationInfo.Subscriber;

import com.google.firebase.messaging.Message;

public interface MessageProvider {
	Message provide(String messageTitle, String messageBody, Subscriber subscriber);
}
