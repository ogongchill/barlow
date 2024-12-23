package com.barlow.storage.db.core;

public interface NotificationInfoProjection {
	Long getMemberNo();
	NotificationTopic getTopic();
	DeviceOs getDeviceOs();
	String getDeviceToken();
}
