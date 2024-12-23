package com.barlow.notification.worker;

import com.barlow.notification.NotificationInfo;
import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.Message;

public class AndroidMessageProvider implements MessageProvider {

	@Override
	public Message provide(String messageTitle, String messageBody, NotificationInfo.Subscriber subscriber) {
		AndroidNotification androidNotification = AndroidNotification.builder()
			.setTitle(messageTitle)
			.setBody(messageBody)
			.setDefaultSound(true)
			.build();
		return Message.builder()
			.setToken(subscriber.token())
			.setAndroidConfig(AndroidConfig.builder().setNotification(androidNotification).build())
			.build();
	}
}
