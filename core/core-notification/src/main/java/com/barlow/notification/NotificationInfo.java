package com.barlow.notification;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class NotificationInfo {

	private final Map<Topic, List<Subscriber>> infos;

	public NotificationInfo(Map<Topic, List<Subscriber>> infos) {
		this.infos = infos;
	}

	void assignBillTotalCountPerTopic(String topic, int totalCount) {
		infos.keySet().stream()
			.filter(info -> info.isSame(topic))
			.forEach(info -> info.setTopicCount(totalCount));
	}

	void assignRepresentationBillAndTotalCount(String representationBillName, int totalCount) {
		infos.keySet().forEach(topic -> {
			topic.setRepresentation(representationBillName);
			topic.setTopicCount(totalCount);
		});
	}

	Map<Topic, List<Subscriber>> getInfos() {
		return infos;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		NotificationInfo that = (NotificationInfo)o;
		return Objects.equals(infos, that.infos);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(infos);
	}

	public static class Topic {

		private final String name;
		private String representation;
		private Integer count;

		private Topic(String name, String representation, Integer count) {
			this.name = name;
			this.representation = representation;
			this.count = count;
		}

		boolean isSame(String topic) {
			return name.equals(topic);
		}

		void setRepresentation(String representation) {
			this.representation = representation;
		}

		void setTopicCount(int count) {
			this.count = count;
		}

		public static Topic initialize(String topic) {
			return new Topic(topic, null, null);
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

		@Override
		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (o == null || getClass() != o.getClass())
				return false;
			Topic topic = (Topic)o;
			return Objects.equals(name, topic.name);
		}

		@Override
		public int hashCode() {
			return Objects.hashCode(name);
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
