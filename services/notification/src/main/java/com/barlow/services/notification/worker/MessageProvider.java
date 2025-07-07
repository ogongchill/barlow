package com.barlow.services.notification.worker;

import com.barlow.core.enumerate.DeviceOs;
import com.barlow.services.notification.NotificationInfo;

public interface MessageProvider {

	NotificationMessage provide(String messageTitle, String messageBody, NotificationInfo.Subscriber subscriber);

	DeviceOs supportedOs();
}
