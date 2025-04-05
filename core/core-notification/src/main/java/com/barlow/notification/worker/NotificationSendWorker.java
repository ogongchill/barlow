package com.barlow.notification.worker;

import static com.barlow.notification.NotificationInfo.Subscriber;
import static com.barlow.notification.NotificationInfo.Topic;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Predicate;

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

	public void invoke(MessageTemplate messageTemplate, Map<Topic, List<Subscriber>> topicsWithSubscribers) {
		CompletableFuture.runAsync(
			billNotificationTask(messageTemplate, topicsWithSubscribers, Subscriber::isIOS,
				new IosMessageProvider(), iosNotificationSender
			), asyncThreadPoolExecutor
		);
		CompletableFuture.runAsync(
			billNotificationTask(messageTemplate, topicsWithSubscribers, Subscriber::isANDROID,
				new AndroidMessageProvider(), androidNotificationSender
			), asyncThreadPoolExecutor
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
				RetryWorker retryWorker = new RetryWorker(asyncThreadPoolExecutor, notificationSender);
				retryWorker.start(notificationResult);
			}
		};
	}
}
