package com.barlow.infra.notification;

public interface MessageTemplate {
	String getMessageTitleFormat(NotificationInfo.Topic topic);
	String getMessageBodyFormat(NotificationInfo.Topic topic);
}
