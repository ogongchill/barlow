package com.barlow.services.notification.worker.android;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.barlow.core.enumerate.DeviceOs;
import com.barlow.services.notification.NotificationInfo;
import com.barlow.services.notification.worker.MessageProvider;
import com.barlow.services.notification.worker.MessageStatus;
import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.Message;

@Component
public class AosMessageProvider implements MessageProvider {

	@Override
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

	@Override
	public DeviceOs supportedOs() {
		return DeviceOs.ANDROID;
	}
}
