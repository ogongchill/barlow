package com.barlow.notification.worker;

import static com.barlow.notification.NotificationInfo.SubscriberDevice;
import static com.barlow.notification.NotificationInfo.Topic;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import org.springframework.stereotype.Component;

import com.barlow.notification.MessageTemplate;
import com.google.firebase.messaging.Message;

@Component
public class NotificationSendWorker {

	private final IOSNotificationSender iosNotificationSender;
	private final AndroidNotificationSender androidNotificationSender;
	private final Executor asyncThreadPoolExecutor;

	public NotificationSendWorker(
		IOSNotificationSender iosNotificationSender,
		AndroidNotificationSender androidNotificationSender,
		Executor asyncThreadPoolExecutor
	) {
		this.iosNotificationSender = iosNotificationSender;
		this.androidNotificationSender = androidNotificationSender;
		this.asyncThreadPoolExecutor = asyncThreadPoolExecutor;
	}

	public void invoke(MessageTemplate messageTemplate, Map<Topic, List<SubscriberDevice>> topicsWithSubscribers) {
		CompletableFuture.runAsync(() -> {
				List<Message> iosMessages = MessageProvider.generateIosMessages(messageTemplate, topicsWithSubscribers);
				iosNotificationSender.send(iosMessages);
			}, asyncThreadPoolExecutor
		);
		CompletableFuture.runAsync(() -> {
				List<Message> androidMessages = MessageProvider.generateAndroidMessages(messageTemplate, topicsWithSubscribers);
				androidNotificationSender.send(androidMessages);
			}, asyncThreadPoolExecutor
		);
	}
}
