package com.barlow.notification.worker;

import static com.barlow.notification.NotificationInfo.SubscriberDevice;
import static com.barlow.notification.NotificationInfo.Topic;

import java.util.List;
import java.util.Map;

import com.barlow.notification.MessageTemplate;
import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.ApnsConfig;
import com.google.firebase.messaging.Aps;
import com.google.firebase.messaging.ApsAlert;
import com.google.firebase.messaging.Message;

public class MessageProvider {

	private static final String ALERT_SOUND = "default";

	private MessageProvider() {
	}

	static List<Message> generateIosMessages(
		MessageTemplate messageTemplate,
		Map<Topic, List<SubscriberDevice>> topicsWithSubscribers
	) {
		return topicsWithSubscribers.entrySet().stream()
			.filter(topicEntry -> topicEntry.getValue().stream().allMatch(SubscriberDevice::isIOS))
			.flatMap(topicEntry -> {
				Topic topic = topicEntry.getKey();
				List<SubscriberDevice> devices = topicEntry.getValue();
				return devices.stream().map(device -> iosProvider(messageTemplate, topic, device));
			})
			.toList();
	}

	static List<Message> generateAndroidMessages(
		MessageTemplate messageTemplate,
		Map<Topic, List<SubscriberDevice>> topicsWithSubscribers
	) {
		return topicsWithSubscribers.entrySet().stream()
			.filter(topicEntry -> topicEntry.getValue().stream().allMatch(SubscriberDevice::isANDROID))
			.flatMap(topicEntry -> {
				Topic topic = topicEntry.getKey();
				List<SubscriberDevice> devices = topicEntry.getValue();
				return devices.stream().map(device -> androidProvider(messageTemplate, topic, device));
			})
			.toList();
	}

	private static Message iosProvider(MessageTemplate template, Topic topic, SubscriberDevice device) {
		ApsAlert apsAlert = ApsAlert.builder()
			.setTitle(String.format(template.getMessageTitleFormat(), topic.getName(), topic.getCount()))
			.setBody(template.getMessageBodyFormat(topic.getName()))
			.build();
		Aps aps = Aps.builder()
			.setAlert(apsAlert)
			.setContentAvailable(true)
			.setSound(ALERT_SOUND)
			.build();
		return Message.builder()
			.setToken(device.token())
			.setApnsConfig(ApnsConfig.builder().setAps(aps).build())
			.build();
	}

	private static Message androidProvider(MessageTemplate template, Topic topic, SubscriberDevice device) {
		AndroidNotification androidNotification = AndroidNotification.builder()
			.setTitle(String.format(template.getMessageTitleFormat(), topic.getName(), topic.getCount()))
			.setBody(template.getMessageBodyFormat(topic.getName()))
			.setDefaultSound(true)
			.build();
		return Message.builder()
			.setToken(device.token())
			.setAndroidConfig(AndroidConfig.builder().setNotification(androidNotification).build())
			.build();
	}
}
