package com.barlow.notification;

public interface MessageTemplate {
	String getMessageTitleFormat();
	String getMessageBodyFormat(String topicName);
}
