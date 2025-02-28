package com.barlow.core.api.controller.v1.menu;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.barlow.core.domain.User;
import com.barlow.core.domain.menu.MenuFacade;
import com.barlow.core.enumerate.LegislationType;
import com.barlow.core.support.annotation.PassportUser;
import com.barlow.core.support.response.ApiResponse;

@RestController
@RequestMapping("/api/v1/menu")
public class MenuController {

	private final MenuFacade menuFacade;

	public MenuController(MenuFacade menuFacade) {
		this.menuFacade = menuFacade;
	}

	@PostMapping("/notifications/{legislationType}/activate")
	public ApiResponse<Void> activateNotify(
		@PathVariable LegislationType legislationType,
		@PassportUser User user
	) {
		menuFacade.activateNotify(legislationType, user);
		return ApiResponse.success();
	}

	@PostMapping("/notifications/{legislationType}/deactivate")
	public ApiResponse<Void> deactivateNotify(
		@PathVariable LegislationType legislationType,
		@PassportUser User user
	) {
		menuFacade.deactivateNotify(legislationType, user);
		return ApiResponse.success();
	}

	@GetMapping("/notifications")
	public ApiResponse<NotificationMenuResponse> retrieveNotifications(@PassportUser User user) {
		return ApiResponse.success(NotificationMenuResponse.from(menuFacade.retrieveNotificationSettingMenu(user)));
	}
}
