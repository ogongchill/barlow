package com.barlow.services.notification.worker;

import static com.barlow.services.notification.NotificationInfo.Subscriber;
import static com.barlow.services.notification.NotificationInfo.Topic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.barlow.core.enumerate.DeviceOs;
import com.barlow.services.notification.MessageTemplate;
import com.barlow.services.notification.NotificationInfo;

@Component
public class NotificationWorkerAdapter implements NotificationWorkerPort {

	private final Map<DeviceOs, NotificationSender> senders;
	private final Map<DeviceOs, MessageProvider> providers;
	private final IdempotencyRepository idempotencyRepository;
	private final NotificationRetryPort retryPort;
	private final Executor executor;

	public NotificationWorkerAdapter(
		List<NotificationSender> senders,
		List<MessageProvider> providers,
		NotificationRetryPort retryPort,
		IdempotencyRepository idempotencyRepository,
		@Qualifier("asyncThreadPoolExecutor") Executor executor
	) {
		this.senders = senders.stream().collect(Collectors.toMap(NotificationSender::supportedOs, s -> s));
		this.providers = providers.stream().collect(Collectors.toMap(MessageProvider::supportedOs, mp -> mp));
		this.idempotencyRepository = idempotencyRepository;
		this.retryPort = retryPort;
		this.executor = executor;
	}

	@Override
	public void notify(MessageTemplate messageTemplate, NotificationInfo notificationInfo) {
		for (DeviceOs deviceOs : senders.keySet()) {
			NotificationInfo info = notificationInfo.filterByOs(deviceOs);
			if (info.isEmpty()) {
				continue;
			}
			CompletableFuture.runAsync(() -> task(messageTemplate, notificationInfo, deviceOs), executor);
		}
	}

	private void task(MessageTemplate messageTemplate, NotificationInfo notificationInfo, DeviceOs deviceOs) {
		NotificationSender sender = senders.get(deviceOs);
		MessageProvider provider = providers.get(deviceOs);
		List<NotificationMessage> messages = new ArrayList<>();
		for (Map.Entry<Topic, List<Subscriber>> entry : notificationInfo.infos().entrySet()) {
			String title = messageTemplate.getMessageTitleFormat(entry.getKey());
			String body = messageTemplate.getMessageBodyFormat(entry.getKey());
			for (Subscriber subscriber : entry.getValue()) {
				NotificationMessage message = provider.provide(title, body, subscriber);
				if (!idempotencyRepository.exists(message.id())) {
					messages.add(message);
					idempotencyRepository.markAsPending(message);
				}
			}
		}

		NotificationResult result = sender.send(messages);
		result.getSuccessMessages().forEach(idempotencyRepository::markAsSuccess);
		if (result.hasRetryableFailure()) {
			result.getRetryableMessages().forEach(idempotencyRepository::markAsFailed);
			retryPort.retry(new RetryMessage(deviceOs, result.groupRetryableFailuresByError()));
		}
	}
}
