package com.barlow.notification;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.barlow.core.enumerate.DeviceOs;
import com.barlow.core.enumerate.NotificationTopic;

public class NotificationInfo {

	private final Map<Topic, List<Subscriber>> infos;

	public NotificationInfo(Map<Topic, List<Subscriber>> infos) {
		this.infos = infos;
	}

	void assignBillTotalCountPerTopic(NotificationTopic topic, int totalCount) {
		infos.keySet().stream()
			.filter(info -> info.isSame(topic))
			.forEach(info -> info.setTopicCount(totalCount));
	}

	void assignRepresentationBillAndTotalCountPerTopic(
		NotificationTopic topic,
		String representationBillName,
		int totalCount
	) {
		infos.keySet().stream()
			.filter(info -> info.isSame(topic))
			.forEach(info -> {
				info.setTopicCount(totalCount);
				info.setRepresentation(representationBillName);
			});
	}

	public Map<Topic, List<Subscriber>> getInfos() {
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

		private final NotificationTopic topic;
		private String representation;
		private Integer count;

		private Topic(NotificationTopic topic, String representation, Integer count) {
			this.topic = topic;
			this.representation = representation;
			this.count = count;
		}

		boolean isSame(NotificationTopic topic) {
			return this.topic.equals(topic);
		}

		boolean isCommitteeType() {
			return this.topic.isRelatedCommittee();
		}

		void setRepresentation(String representation) {
			this.representation = representation;
		}

		void setTopicCount(int count) {
			this.count = count;
		}

		public static Topic initialize(NotificationTopic topic) {
			return new Topic(topic, null, null);
		}

		NotificationTopic getTopic() {
			return topic;
		}

		String getRepresentation() {
			return representation;
		}

		Integer getCount() {
			return count;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (o == null || getClass() != o.getClass())
				return false;
			Topic topic = (Topic)o;
			return Objects.equals(this.topic, topic.topic);
		}

		@Override
		public int hashCode() {
			return Objects.hashCode(topic);
		}
	}

	public record Subscriber(
		Long memberNo,
		DeviceOs os,
		String token
	) {
		public boolean isIOS() {
			return os.isIOS();
		}

		public boolean isANDROID() {
			return os.isANDROID();
		}
	}
}
