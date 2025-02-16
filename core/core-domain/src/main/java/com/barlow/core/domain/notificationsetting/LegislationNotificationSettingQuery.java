package com.barlow.core.domain.notificationsetting;

import com.barlow.core.domain.User;

public record LegislationNotificationSettingQuery(
	String committeeName,
	User user
) {
}
