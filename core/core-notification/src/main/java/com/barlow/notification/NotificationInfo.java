package com.barlow.notification;

public class NotificationInfo {

	private final Topic topic;
	private final Subscriber subscriber;

	public NotificationInfo(Topic topic, Subscriber subscriber) {
		this.topic = topic;
		this.subscriber = subscriber;
	}

	private NotificationInfo(Long memberNo, String topic, String deviceOs, String deviceToken) {
		this(new Topic(topic, null, null), new Subscriber(memberNo, deviceOs, deviceToken));
	}

	public static NotificationInfo initialize(Long memberNo, String topic, String deviceOs, String deviceToken) {
		return new NotificationInfo(memberNo, topic, deviceOs, deviceToken);
	}

	boolean isSameTopic(String topic) {
		return this.topic.isSame(topic);
	}

	Topic getTopic() {
		return topic;
	}

	Subscriber getSubscriberDevice() {
		return subscriber;
	}

	void setRepresentation(String representation) {
		this.topic.representation = representation;
	}

	void setTopicCount(int count) {
		this.topic.count = count;
	}

	public static class Topic {

		private final String name;
		private String representation;
		private Integer count;

		public Topic(String name, String representation, Integer count) {
			this.name = name;
			this.representation = representation;
			this.count = count;
		}

		public String getName() {
			return name;
		}

		public String getRepresentation() {
			return representation;
		}

		public Integer getCount() {
			return count;
		}

		public boolean isSame(String topic) {
			return name.equals(topic);
		}
	}

	public record Subscriber(Long memberNo, String os, String token) {
		private static final String IOS = "IOS";
		private static final String ANDROID = "ANDROID";

		public boolean isIOS() {
			return IOS.equals(this.os);
		}

		public boolean isANDROID() {
			return ANDROID.equals(this.os);
		}
	}
}
