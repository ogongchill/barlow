package com.barlow.notification;

import static com.barlow.notification.NotificationInfo.SubscriberDevice;
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
		BillNotificationPayload notificationPayload = checkAndConvert(payload);
		MessageTemplate messageTemplate = MessageTemplateFactory.getBy(notificationPayload.type());
		NotificationInfoReader reader = notificationInfoReaderFactory.getBy(notificationPayload.type());
		Map<Topic, List<SubscriberDevice>> topicsWithSubscribers = reader.readNotificationInfos(notificationPayload)
			.stream()
			.collect(Collectors.groupingBy(
				NotificationInfo::getTopic,
				Collectors.mapping(NotificationInfo::getSubscriberDevice, Collectors.toList())
			));
		notificationSendWorker.invoke(messageTemplate, topicsWithSubscribers);
	}

	private BillNotificationPayload checkAndConvert(NotificationPayload payload) {
		if (payload instanceof BillNotificationPayload) {
			return (BillNotificationPayload)payload;
		} else {
			throw new IllegalArgumentException("Payload must be BillNotificationPayload");
		}
	}
}
