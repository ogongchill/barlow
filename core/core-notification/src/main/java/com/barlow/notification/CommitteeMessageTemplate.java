package com.barlow.notification;

public class CommitteeMessageTemplate implements MessageTemplate {

	private static final String MESSAGE_TITLE_FORMAT = "%s 에서 알려드려요";
	private static final String MESSAGE_BODY_FORMAT = "%s에서 %d 개의 법안을 새롭게 검토하고 있어요.";

	@Override
	public String getMessageTitleFormat(NotificationInfo.Topic topic) {
		return String.format(MESSAGE_TITLE_FORMAT, topic.getName());
	}

	@Override
	public String getMessageBodyFormat(NotificationInfo.Topic topic) {
		return String.format(MESSAGE_BODY_FORMAT, topic.getName(), topic.getCount());
	}
}
