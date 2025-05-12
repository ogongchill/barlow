package com.barlow.app.api.controller.v1.legislationaccount;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.barlow.core.domain.User;
import com.barlow.core.domain.notificationsetting.NotificationSettingService;
import com.barlow.core.enumerate.LegislationType;
import com.barlow.app.support.response.ApiResponse;
import com.barlow.services.auth.support.annotation.PassportUser;

@RestController
@RequestMapping("/api/v1/legislation-accounts/{legislationType}/notification-setting")
public class LegislationAccountNotificationSettingController {

	private static final Logger log = LoggerFactory.getLogger(LegislationAccountNotificationSettingController.class);

	private final NotificationSettingService notificationSettingService;

	public LegislationAccountNotificationSettingController(NotificationSettingService notificationSettingService) {
		this.notificationSettingService = notificationSettingService;
	}

	@PostMapping("/activate")
	public ApiResponse<Void> activate(
		@PathVariable("legislationType") LegislationType legislationType,
		@PassportUser User user
	) {
		log.info("Received {} account notification setting activated", legislationType);
		notificationSettingService.activateSetting(legislationType, user);
		return ApiResponse.success();
	}

	@PostMapping("/deactivate")
	public ApiResponse<Void> deactivate(
		@PathVariable("legislationType") LegislationType legislationType,
		@PassportUser User user
	) {
		log.info("Received {} account notification setting deactivated", legislationType);
		notificationSettingService.deactivateSetting(legislationType, user);
		return ApiResponse.success();
	}
}
