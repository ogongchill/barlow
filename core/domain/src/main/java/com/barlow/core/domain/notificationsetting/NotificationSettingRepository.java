package com.barlow.core.domain.notificationsetting;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Repository;

import com.barlow.core.domain.User;

@Repository
public interface NotificationSettingRepository {

	@NotNull
	NotificationSetting retrieveNotificationSetting(LegislationNotificationSettingQuery query);

	List<NotificationSetting> retrieveNotificationSettings(User user);

	void saveNotificationSetting(NotificationSetting notificationSetting);

	void deleteNotificationSetting(NotificationSetting notificationSetting);
}
