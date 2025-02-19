package com.barlow.storage.db.core;

import com.barlow.core.enumerate.DeviceOs;
import com.barlow.core.enumerate.NotificationTopic;

public record NotificationInfoProjection(
	Long memberNo,
	NotificationTopic topic,
	DeviceOs deviceOs,
	String deviceToken
) {
}
