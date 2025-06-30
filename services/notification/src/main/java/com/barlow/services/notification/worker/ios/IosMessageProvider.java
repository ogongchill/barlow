package com.barlow.services.notification.worker.ios;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.barlow.core.enumerate.DeviceOs;
import com.barlow.services.notification.NotificationInfo;
import com.barlow.services.notification.worker.MessageProvider;
import com.barlow.services.notification.worker.MessageStatus;
import com.google.firebase.messaging.ApnsConfig;
import com.google.firebase.messaging.Aps;
import com.google.firebase.messaging.ApsAlert;
import com.google.firebase.messaging.Message;

@Component
public class IosMessageProvider implements MessageProvider {

	private static final String ALERT_SOUND = "default";

	@Override
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

	@Override
	public DeviceOs supportedOs() {
		return DeviceOs.IOS;
	}
}
