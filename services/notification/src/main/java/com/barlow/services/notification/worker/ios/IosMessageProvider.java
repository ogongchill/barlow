package com.barlow.services.notification.worker;

import java.util.UUID;

import com.barlow.services.notification.NotificationInfo;
import com.google.firebase.messaging.ApnsConfig;
import com.google.firebase.messaging.Aps;
import com.google.firebase.messaging.ApsAlert;
import com.google.firebase.messaging.Message;

public class IosMessageProvider {

	private static final String ALERT_SOUND = "default";

	public IosMessage provide(String messageTitle, String messageBody, NotificationInfo.Subscriber subscriber) {
		ApsAlert apsAlert = ApsAlert.builder()
			.setTitle(messageTitle)
			.setBody(messageBody)
			.build();
		Aps aps = Aps.builder()
			.setAlert(apsAlert)
			.setContentAvailable(true)
			.setSound(ALERT_SOUND)
			.build();
		return new IosMessage(
			UUID.randomUUID().toString(),
			subscriber.token(),
			subscriber.os(),
			subscriber.memberNo(),
			Message.builder()
				.setToken(subscriber.token())
				.setApnsConfig(ApnsConfig.builder().setAps(aps).build())
				.build(),
			MessageStatus.CREATED
		);
	}
}
