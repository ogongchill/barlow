package com.barlow.notification.worker;

import com.barlow.notification.NotificationInfo;
import com.google.firebase.messaging.ApnsConfig;
import com.google.firebase.messaging.Aps;
import com.google.firebase.messaging.ApsAlert;
import com.google.firebase.messaging.Message;

public class IosMessageProvider implements MessageProvider {

	private static final String ALERT_SOUND = "default";

	@Override
	public Message provide(String messageTitle, String messageBody, NotificationInfo.Subscriber subscriber) {
		ApsAlert apsAlert = ApsAlert.builder()
			.setTitle(messageTitle)
			.setBody(messageBody)
			.build();
		Aps aps = Aps.builder()
			.setAlert(apsAlert)
			.setContentAvailable(true)
			.setSound(ALERT_SOUND)
			.build();
		return Message.builder()
			.setToken(subscriber.token())
			.setApnsConfig(ApnsConfig.builder().setAps(aps).build())
			.build();
	}
}
