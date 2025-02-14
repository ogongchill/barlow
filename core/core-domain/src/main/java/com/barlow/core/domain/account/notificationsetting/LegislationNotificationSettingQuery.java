package com.barlow.core.domain.account.notificationsetting;

import com.barlow.core.domain.User;

public record LegislationNotificationSettingQuery(
	String committeeName,
	User user
) {
}
