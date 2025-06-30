package com.barlow.services.notification.worker;

import java.util.UUID;

import com.barlow.services.notification.NotificationInfo;
import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.Message;

public class AosMessageProvider {

	public AosMessage provide(String messageTitle, String messageBody, NotificationInfo.Subscriber subscriber) {
		AndroidNotification androidNotification = AndroidNotification.builder()
			.setTitle(messageTitle)
			.setBody(messageBody)
			.setDefaultSound(true)
			.build();
		return new AosMessage(
			UUID.randomUUID().toString(),
			subscriber.token(),
			subscriber.os(),
			subscriber.memberNo(),
			Message.builder()
				.setToken(subscriber.token())
				.setAndroidConfig(AndroidConfig.builder().setNotification(androidNotification).build())
				.build(),
			MessageStatus.CREATED
		);
	}
}
