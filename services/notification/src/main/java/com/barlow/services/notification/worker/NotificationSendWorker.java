package com.barlow.services.notification.worker;

import static com.barlow.services.notification.NotificationInfo.Subscriber;
import static com.barlow.services.notification.NotificationInfo.Topic;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.barlow.services.notification.MessageTemplate;
import com.google.firebase.messaging.Message;

@Component
public class NotificationSendWorker {

	private final IOSNotificationSender iosNotificationSender;
	private final AndroidNotificationSender androidNotificationSender;
	private final Executor sendExecutor;

	public NotificationSendWorker(
		IOSNotificationSender iosNotificationSender,
		AndroidNotificationSender androidNotificationSender,
		@Qualifier("asyncThreadPoolExecutor") Executor sendExecutor
	) {
		this.iosNotificationSender = iosNotificationSender;
		this.androidNotificationSender = androidNotificationSender;
		this.sendExecutor = sendExecutor;
	}

	public void invoke(MessageTemplate messageTemplate, Map<Topic, List<Subscriber>> topicsWithSubscribers) {
		CompletableFuture.runAsync(
			billNotificationTask(messageTemplate, topicsWithSubscribers, Subscriber::isIOS,
				new IosMessageProvider(), iosNotificationSender
			), sendExecutor
		);
		CompletableFuture.runAsync(
			billNotificationTask(messageTemplate, topicsWithSubscribers, Subscriber::isANDROID,
				new AndroidMessageProvider(), androidNotificationSender
			), sendExecutor
		);
	}

	private Runnable billNotificationTask(
		MessageTemplate messageTemplate,
		Map<Topic, List<Subscriber>> topicsWithSubscribers,
		Predicate<Subscriber> subscriberOsPredicate,
		MessageProvider messageProvider,
		NotificationSender notificationSender
	) {
		return () -> {
			List<Message> messages = topicsWithSubscribers.entrySet().stream()
				.flatMap(entry -> entry.getValue().stream()
					.filter(subscriberOsPredicate)
					.map(subscriber -> messageProvider.provide(
						messageTemplate.getMessageTitleFormat(entry.getKey()),
						messageTemplate.getMessageBodyFormat(entry.getKey()),
						subscriber
					))
				)
				.toList();
			NotificationResult notificationResult = notificationSender.send(messages);
			if (notificationResult.hasRetryableFailure()) {
				RetryWorker retryWorker = new RetryWorker(sendExecutor, notificationSender);
				retryWorker.start(notificationResult);
			}
		};
	}
}
