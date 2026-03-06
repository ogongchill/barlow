package com.barlow.app.api.controller.v1.menu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.barlow.core.domain.Passport;
import com.barlow.core.service.menu.MenuFacade;
import com.barlow.core.enumerate.LegislationType;
import com.barlow.app.support.response.ApiResponse;
import com.barlow.infra.auth.support.annotation.PassportUser;

@RestController
@RequestMapping("/api/v1/menu")
public class MenuController {

	private static final Logger log = LoggerFactory.getLogger(MenuController.class);

	private final MenuFacade menuFacade;

	public MenuController(MenuFacade menuFacade) {
		this.menuFacade = menuFacade;
	}

	@PatchMapping("/notifications/{legislationType}")
	public ApiResponse<Void> updateNotify(@PathVariable LegislationType legislationType,
		@PassportUser Passport passport,
		@RequestBody NotificationToggleRequest request) {
		log.info("Received {} account notification setting update: active={} for user {}", legislationType,
			request.active(), passport.getUserNo());
		if (request.active()) {
			menuFacade.activateNotify(legislationType, passport.getUser());
		} else {
			menuFacade.deactivateNotify(legislationType, passport.getUser());
		}
		return ApiResponse.success();
	}

	@GetMapping("/notifications")
	public ApiResponse<NotificationMenuResponse> retrieveNotifications(@PassportUser Passport passport) {
		log.info("Received user {} notification setting request.", passport.getUserNo());
		return ApiResponse
			.success(NotificationMenuResponse.from(menuFacade.retrieveNotificationSettingMenu(passport.getUser())));
	}
}
