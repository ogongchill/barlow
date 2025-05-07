package com.barlow.services.notification;

public class MessageTemplateFactory {

	private MessageTemplateFactory() {
	}

	public static MessageTemplate getBy(NotificationType type) {
		if (type.isCommittee()) {
			return new CommitteeMessageTemplate();
		} else {
			return new DefaultMessageTemplate();
		}
	}
}
