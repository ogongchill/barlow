package com.barlow.core.domain.notificationsetting;

import com.barlow.core.domain.User;
import com.barlow.core.enumerate.LegislationType;

public record LegislationNotificationSettingQuery(
	LegislationType legislationType,
	User user
) {
}
