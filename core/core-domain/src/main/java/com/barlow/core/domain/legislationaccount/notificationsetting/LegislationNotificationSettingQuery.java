package com.barlow.core.domain.legislationaccount.notificationsetting;

import com.barlow.core.domain.User;

public record LegislationNotificationSettingQuery(
	String committeeName,
	User user
) {
}
