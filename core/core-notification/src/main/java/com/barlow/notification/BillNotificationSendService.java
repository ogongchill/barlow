package com.barlow.notification;

import org.springframework.stereotype.Component;

import com.barlow.notification.worker.NotificationSendWorker;

@Component
public class BillNotificationSendService implements NotificationSendPort {

	private final NotificationInfoReaderFactory notificationInfoReaderFactory;
	private final NotificationSendWorker notificationSendWorker;
	private final NotificationCenterRegistrar notificationCenterRegistrar;

	public BillNotificationSendService(
		NotificationInfoReaderFactory notificationInfoReaderFactory,
		NotificationSendWorker notificationSendWorker,
		NotificationCenterRegistrar notificationCenterRegistrar
	) {
		this.notificationInfoReaderFactory = notificationInfoReaderFactory;
		this.notificationSendWorker = notificationSendWorker;
		this.notificationCenterRegistrar = notificationCenterRegistrar;
	}

	@Override
	public void sendCall(NotificationRequest request) {
		MessageTemplate messageTemplate = MessageTemplateFactory.getBy(request.type());
		NotificationInfoReader reader = notificationInfoReaderFactory.getBy(request.type());
		NotificationInfo notificationInfo = reader.readNotificationInfos(request);
		notificationSendWorker.invoke(messageTemplate, notificationInfo.getInfos());
		notificationCenterRegistrar.register(notificationInfo, request);
	}
}
