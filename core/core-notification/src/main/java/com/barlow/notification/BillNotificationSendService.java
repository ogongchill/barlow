package com.barlow.notification;

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
		NotificationInfo notificationInfo = reader.readNotificationInfos(payload);
		notificationSendWorker.invoke(messageTemplate, notificationInfo.getInfos());
	}
}
