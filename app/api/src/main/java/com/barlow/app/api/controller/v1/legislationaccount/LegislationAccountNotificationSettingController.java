package com.barlow.app.api.controller.v1.legislationaccount;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.barlow.core.domain.Passport;
import com.barlow.core.service.notificationsetting.NotificationSettingService;
import com.barlow.core.enumerate.LegislationType;
import com.barlow.app.support.response.ApiResponse;
import com.barlow.infra.auth.support.annotation.PassportUser;

@RestController
@RequestMapping("/api/v1/legislation-accounts/{legislationType}/notification-settings")
public class LegislationAccountNotificationSettingController {

	private static final Logger log = LoggerFactory.getLogger(LegislationAccountNotificationSettingController.class);

	private final NotificationSettingService notificationSettingService;

	public LegislationAccountNotificationSettingController(NotificationSettingService notificationSettingService) {
		this.notificationSettingService = notificationSettingService;
	}

	@PatchMapping
	public ApiResponse<Void> updateNotificationSetting(
		@PathVariable("legislationType") LegislationType legislationType,
		@PassportUser Passport passport,
		@RequestBody NotificationSettingRequest request) {
		log.info("Received {} account notification setting update: active={}", legislationType, request.active());
		if (request.active()) {
			notificationSettingService.activateSetting(legislationType, passport.getUser());
		} else {
			notificationSettingService.deactivateSetting(legislationType, passport.getUser());
		}
		return ApiResponse.success();
	}
}
