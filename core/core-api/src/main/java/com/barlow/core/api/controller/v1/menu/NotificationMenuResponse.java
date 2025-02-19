package com.barlow.core.api.controller.v1.menu;

import java.util.List;

import com.barlow.core.domain.notificationsetting.NotificationSetting;
import com.barlow.core.support.response.Constant;

public record NotificationMenuResponse(
	String title,
	List<Setting> settings
) {
	static NotificationMenuResponse from(List<NotificationSetting> notificationSettings) {
		return new NotificationMenuResponse(
			"알림 설정",
			notificationSettings.stream()
				.map(Setting::from)
				.toList()
		);
	}

	record Setting(
		String name,
		String iconPath,
		boolean isEnable
	) {
		static Setting from(NotificationSetting notificationSetting) {
			return new Setting(
				notificationSetting.getTopicName(),
				Constant.IMAGE_ACCESS_URL + notificationSetting.getIconPath(),
				notificationSetting.isNotifiable()
			);
		}
	}
}
