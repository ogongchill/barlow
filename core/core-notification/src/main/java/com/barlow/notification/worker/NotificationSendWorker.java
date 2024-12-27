package com.barlow.notification.worker;

import static com.barlow.notification.NotificationInfo.Subscriber;
import static com.barlow.notification.NotificationInfo.Topic;

import java.util.ArrayList;
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
	private final NotificationCenterRegistrar notificationCenterRegistrar;
	private final Executor asyncThreadPoolExecutor;

	public NotificationSendWorker(
		IOSNotificationSender iosNotificationSender,
		AndroidNotificationSender androidNotificationSender,
		NotificationCenterRegistrar notificationCenterRegistrar,
		Executor asyncThreadPoolExecutor
	) {
		this.iosNotificationSender = iosNotificationSender;
		this.androidNotificationSender = androidNotificationSender;
		this.notificationCenterRegistrar = notificationCenterRegistrar;
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
		List<Message> messages = new ArrayList<>();
		List<NotificationCenterInfo> notificationCenterInfos = new ArrayList<>();
		return () -> {
			topicsWithSubscribers.forEach((topic, subscribers) -> {
				String title = messageTemplate.getMessageTitleFormat(topic);
				String body = messageTemplate.getMessageBodyFormat(topic);
				subscribers.stream()
					.filter(subscriberOsPredicate)
					.forEach(subscriber -> {
						notificationCenterInfos.add(new NotificationCenterInfo(subscriber, topic, title, body));
						messages.add(messageProvider.provide(title, body, subscriber));
					});
			});
			notificationCenterRegistrar.registerAll(notificationCenterInfos);
			notificationSender.send(messages);
		};
	}
}
