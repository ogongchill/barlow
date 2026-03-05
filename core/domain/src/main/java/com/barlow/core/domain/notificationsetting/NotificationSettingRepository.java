package com.barlow.core.domain.notificationsetting;

import java.util.List;

import com.barlow.core.domain.User;

public interface NotificationSettingRepository {

	NotificationSetting retrieveNotificationSetting(LegislationNotificationSettingQuery query);

	List<NotificationSetting> retrieveNotificationSettings(User user);

	void saveNotificationSetting(NotificationSetting notificationSetting);

	void deleteNotificationSetting(NotificationSetting notificationSetting);
}
