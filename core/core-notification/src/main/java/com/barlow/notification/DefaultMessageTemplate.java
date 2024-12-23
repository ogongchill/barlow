package com.barlow.notification;

import java.util.Arrays;

public class DefaultMessageTemplate implements MessageTemplate {

	private static final String MESSAGE_TITLE_FORMAT = "바로에서 바로 알려드려요";

	@Override
	public String getMessageTitleFormat() {
		return MESSAGE_TITLE_FORMAT;
	}

	@Override
	public String getMessageBodyFormat(String topicName) {
		return Default.findByValue(topicName).bodyFormat;
	}

	enum Default {

		RECEIPT("접수의안", "%s 등 %d 개의 법안이 오늘 접수되었어요. 지금 바로 확인해보세요."),
		SUBMISSION_PLENARY_SESSION("본회의부의안건", "%s 등 %d 개의 법안이 지금 본회의에서 검토중이에요."),
		RESOLUTION_PLENARY_SESSION("본회의의결", "%s 등 %d 개의 법안이 오늘 본회의에서 통과되었어요."),
		RECONSIDERATION_GOVERNMENT("재의요구", "정부에서 %s 등 %d 개의 법안에 대해 재의를 요구했어요."),
		PROMULGATION("공포", "오늘 %s 등 %d 개의 법안이 공포되었어요."),
		;

		private final String value;
		private final String bodyFormat;

		Default(String value, String bodyFormat) {
			this.value = value;
			this.bodyFormat = bodyFormat;
		}

		static Default findByValue(String topicName) {
			return Arrays.stream(Default.values())
				.filter(v -> v.value.equals(topicName))
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException(""));
		}
	}
}
