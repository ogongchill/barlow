package com.barlow.app.api.controller.v1.menu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.barlow.core.domain.User;
import com.barlow.core.domain.menu.MenuFacade;
import com.barlow.core.enumerate.LegislationType;
import com.barlow.app.support.response.ApiResponse;
import com.barlow.services.auth.support.annotation.PassportUser;

@RestController
@RequestMapping("/api/v1/menu")
public class MenuController {

	private static final Logger log = LoggerFactory.getLogger(MenuController.class);

	private final MenuFacade menuFacade;

	public MenuController(MenuFacade menuFacade) {
		this.menuFacade = menuFacade;
	}

	@PostMapping("/notifications/{legislationType}/activate")
	public ApiResponse<Void> activateNotify(
		@PathVariable LegislationType legislationType,
		@PassportUser User user
	) {
		log.info("Received {} account notification setting activated for user {}", legislationType, user.getUserNo());
		menuFacade.activateNotify(legislationType, user);
		return ApiResponse.success();
	}

	@PostMapping("/notifications/{legislationType}/deactivate")
	public ApiResponse<Void> deactivateNotify(
		@PathVariable LegislationType legislationType,
		@PassportUser User user
	) {
		log.info("Received {} account notification setting deactivated for user {}", legislationType, user.getUserNo());
		menuFacade.deactivateNotify(legislationType, user);
		return ApiResponse.success();
	}

	@GetMapping("/notifications")
	public ApiResponse<NotificationMenuResponse> retrieveNotifications(@PassportUser User user) {
		log.info("Received user {} notification setting request.", user.getUserNo());
		return ApiResponse.success(NotificationMenuResponse.from(menuFacade.retrieveNotificationSettingMenu(user)));
	}
}
