package com.barlow.notification;

public class CommitteeMessageTemplate implements MessageTemplate {

	private static final String MESSAGE_TITLE_FORMAT = "%s 에서 알려드려요";
	private static final String MESSAGE_BODY_FORMAT = "%s에서 %d 개의 법안을 새롭게 검토하고 있어요.";

	@Override
	public String getMessageTitleFormat() {
		return MESSAGE_TITLE_FORMAT;
	}

	@Override
	public String getMessageBodyFormat(String topicName) {
		return MESSAGE_BODY_FORMAT;
	}
}
