package com.barlow.notification;

public interface MessageTemplate {
	String getMessageTitleFormat(NotificationInfo.Topic topic);
	String getMessageBodyFormat(NotificationInfo.Topic topic);
}
