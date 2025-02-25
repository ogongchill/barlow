package com.barlow.notification;

import java.util.Arrays;

import com.barlow.core.enumerate.NotificationTopic;

public class DefaultMessageTemplate implements MessageTemplate {

	private static final String DEFAULT_MESSAGE_TITLE_FORMAT = "바로에서 바로 알려드려요";

	@Override
	public String getMessageTitleFormat(NotificationInfo.Topic topic) {
		if (topic.isCommitteeType()) {
			return String.format(Default.COMMITTEE_RECEIVED.titleFormat, topic.getTopic().getValue());
		}
		return DEFAULT_MESSAGE_TITLE_FORMAT;
	}

	@Override
	public String getMessageBodyFormat(NotificationInfo.Topic topic) {
		if (topic.isCommitteeType()) {
			return String.format(
				Default.COMMITTEE_RECEIVED.bodyFormat,
				topic.getTopic().getValue(),
				topic.getRepresentation(),
				topic.getCount()
			);
		}
		return String.format(
			Default.findByValue(topic.getTopic().getValue()).bodyFormat,
			topic.getRepresentation(),
			topic.getCount()
		);
	}

	enum Default {

		RECEIPT(NotificationTopic.RECEIPT.getValue(), DEFAULT_MESSAGE_TITLE_FORMAT,
			"%s 등 %d 개의 법안이 오늘 접수되었어요. 지금 바로 확인해보세요."),
		SUBMISSION_PLENARY_SESSION(NotificationTopic.SUBMISSION_PLENARY_SESSION.getValue(),
			DEFAULT_MESSAGE_TITLE_FORMAT,
			"%s 등 %d 개의 법안이 지금 본회의에서 검토중이에요."),
		RESOLUTION_PLENARY_SESSION(NotificationTopic.RESOLUTION_PLENARY_SESSION.getValue(),
			DEFAULT_MESSAGE_TITLE_FORMAT,
			"%s 등 %d 개의 법안이 오늘 본회의에서 통과되었어요."),
		RECONSIDERATION_GOVERNMENT(NotificationTopic.RECONSIDERATION_GOVERNMENT.getValue(),
			DEFAULT_MESSAGE_TITLE_FORMAT,
			"정부에서 %s 등 %d 개의 법안에 대해 재의를 요구했어요."),
		PROMULGATION(NotificationTopic.PROMULGATION.getValue(), DEFAULT_MESSAGE_TITLE_FORMAT,
			"%s 등 %d 개의 법안이 공포되었어요."),

		COMMITTEE_RECEIVED("소관위접수", "%s에서 알려드려요",
			"%s에서 %s 등 %d개의 법안을 새롭게 검토하고 있어요."),
		;

		private final String value;
		private final String titleFormat;
		private final String bodyFormat;

		Default(String value, String titleFormat, String bodyFormat) {
			this.value = value;
			this.titleFormat = titleFormat;
			this.bodyFormat = bodyFormat;
		}

		static Default findByValue(String topicValue) {
			return Arrays.stream(Default.values())
				.filter(v -> v.value.equals(topicValue))
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 Default 법안 알림 주제입니다"));
		}
	}
}
