package com.barlow.notification;

public record NotificationCenterInfo(
	Long memberNo,
	String topic,
	String title,
	String body
) {
	public NotificationCenterInfo(
		NotificationInfo.Subscriber subscriber,
		NotificationInfo.Topic topic,
		String title,
		String body
	) {
		this(subscriber.memberNo(), topic.getName(), title, body);
	}
}
