package com.barlow.notification;

public class NotificationInfo {

	private static final int INITIAL_COUNT = 0;

	private final Long memberNo;
	private final Topic topic;
	private final SubscriberDevice subscriberDevice;

	public NotificationInfo(Long memberNo, Topic topic, SubscriberDevice subscriberDevice) {
		this.memberNo = memberNo;
		this.topic = topic;
		this.subscriberDevice = subscriberDevice;
	}

	public NotificationInfo(Long memberNo, String topic, String deviceOs, String deviceToken) {
		this(memberNo, new Topic(topic, INITIAL_COUNT), new SubscriberDevice(deviceOs, deviceToken));
	}

	boolean isSameTopic(String topic) {
		return this.topic.isSame(topic);
	}

	Long getMemberNo() {
		return memberNo;
	}

	Topic getTopic() {
		return topic;
	}

	SubscriberDevice getSubscriberDevice() {
		return subscriberDevice;
	}

	void setTopicCount(int count) {
		this.topic.count = count;
	}

	public static class Topic {

		private final String name;
		private int count;

		public Topic(String name, int count) {
			this.name = name;
			this.count = count;
		}

		public String getName() {
			return name;
		}

		public int getCount() {
			return count;
		}

		public boolean isSame(String topic) {
			return name.equals(topic);
		}
	}

	public record SubscriberDevice(String os, String token) {
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
