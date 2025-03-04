package com.barlow.core.api.controller.v1.legislationaccount;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.barlow.core.domain.User;
import com.barlow.core.domain.notificationsetting.NotificationSettingService;
import com.barlow.core.enumerate.LegislationType;
import com.barlow.core.support.annotation.PassportUser;
import com.barlow.core.support.response.ApiResponse;

@RestController
@RequestMapping("/api/v1/legislation-accounts/{legislationType}/notification-setting")
public class LegislationAccountNotificationSettingController {

	private final NotificationSettingService notificationSettingService;

	public LegislationAccountNotificationSettingController(NotificationSettingService notificationSettingService) {
		this.notificationSettingService = notificationSettingService;
	}

	@PostMapping("/activate")
	public ApiResponse<Void> activate(
		@PathVariable("legislationType") LegislationType legislationType,
		@PassportUser User user
	) {
		notificationSettingService.activateSetting(legislationType, user);
		return ApiResponse.success();
	}

	@PostMapping("/deactivate")
	public ApiResponse<Void> deactivate(
		@PathVariable("legislationType") LegislationType legislationType,
		@PassportUser User user
	) {
		notificationSettingService.deactivateSetting(legislationType, user);
		return ApiResponse.success();
	}
}
