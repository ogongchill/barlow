package com.barlow.storage.db.core;

public record NotificationInfoProjection(
	Long memberNo,
	NotificationTopic topic,
	DeviceOs deviceOs,
	String deviceToken
) {
}
