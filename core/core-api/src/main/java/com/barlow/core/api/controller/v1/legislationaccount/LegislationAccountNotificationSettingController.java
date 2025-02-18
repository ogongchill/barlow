package com.barlow.core.api.controller.v1.legislationaccount;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.barlow.core.domain.User;
import com.barlow.core.domain.notificationsetting.NotificationSettingService;
import com.barlow.core.support.response.ApiResponse;

@RestController
@RequestMapping("/api/v1/legislation-accounts/notification-setting")
public class LegislationAccountNotificationSettingController {

	private final NotificationSettingService notificationSettingService;

	public LegislationAccountNotificationSettingController(NotificationSettingService notificationSettingService) {
		this.notificationSettingService = notificationSettingService;
	}

	@PostMapping("/{committeeName}")
	public ApiResponse<Void> subscribe(@PathVariable String committeeName, User user) {
		notificationSettingService.activateSetting(committeeName, user);
		return ApiResponse.success();
	}

	@PostMapping("/{committeeName}")
	public ApiResponse<Void> unsubscribe(@PathVariable String committeeName, User user) {
		notificationSettingService.deactivateSetting(committeeName, user);
		return ApiResponse.success();
	}
}
