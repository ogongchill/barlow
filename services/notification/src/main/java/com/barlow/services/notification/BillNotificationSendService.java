package com.barlow.services.notification;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.barlow.services.notification.worker.NotificationWorkerPort;

@Component
public class BillNotificationSendService implements NotificationSendPort {

	private final Map<NotificationType, NotificationInfoReader> readers;
	private final Map<NotificationType, MessageTemplate> messageTemplates;
	private final NotificationWorkerPort notificationWorkerPort;
	private final NotificationCenterRegistrar notificationCenterRegistrar;

	public BillNotificationSendService(
		List<NotificationInfoReader> readers,
		List<MessageTemplate> templates,
		NotificationWorkerPort notificationWorkerPort,
		NotificationCenterRegistrar notificationCenterRegistrar
	) {
		this.readers = readers.stream().collect(Collectors.toMap(NotificationInfoReader::supportedType, r -> r));
		this.messageTemplates = templates.stream().collect(Collectors.toMap(MessageTemplate::supportedType, t -> t));
		this.notificationWorkerPort = notificationWorkerPort;
		this.notificationCenterRegistrar = notificationCenterRegistrar;
	}

	@Override
	public void sendCall(NotificationRequest request) {
		MessageTemplate messageTemplate = messageTemplates.get(request.type());
		NotificationInfoReader reader = readers.get(request.type());
		for (int page = 0; ; page++) {
			NotificationInfo notificationInfo = reader.readNotificationInfos(request, page);
			notificationWorkerPort.notify(messageTemplate, notificationInfo);
			notificationCenterRegistrar.register(notificationInfo, request);
			if (notificationInfo.isLast()) {
				break;
			}
		}
	}
}
