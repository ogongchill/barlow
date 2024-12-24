package com.barlow.notification;

import static com.barlow.notification.NotificationInfo.Subscriber;
import static com.barlow.notification.NotificationInfo.Topic;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.barlow.notification.worker.NotificationSendWorker;

@Component
public class BillNotificationSendService implements NotificationSendPort {

	private final NotificationInfoReaderFactory notificationInfoReaderFactory;
	private final NotificationSendWorker notificationSendWorker;

	public BillNotificationSendService(
		NotificationInfoReaderFactory notificationInfoReaderFactory,
		NotificationSendWorker notificationSendWorker
	) {
		this.notificationInfoReaderFactory = notificationInfoReaderFactory;
		this.notificationSendWorker = notificationSendWorker;
	}

	@Override
	public void sendCall(NotificationPayload payload) {
		MessageTemplate messageTemplate = MessageTemplateFactory.getBy(payload.type());
		NotificationInfoReader reader = notificationInfoReaderFactory.getBy(payload.type());
		Map<Topic, List<Subscriber>> topicsWithSubscribers = reader.readNotificationInfos(payload)
			.stream()
			.collect(Collectors.groupingBy(
				NotificationInfo::getTopic,
				Collectors.mapping(NotificationInfo::getSubscriberDevice, Collectors.toList())
			));
		notificationSendWorker.invoke(messageTemplate, topicsWithSubscribers);
	}
}
