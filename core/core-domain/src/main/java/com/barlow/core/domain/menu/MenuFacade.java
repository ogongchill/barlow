package com.barlow.core.domain.menu;

import java.util.List;

import org.springframework.stereotype.Service;

import com.barlow.core.domain.User;
import com.barlow.core.domain.notificationsetting.NotificationSetting;
import com.barlow.core.domain.notificationsetting.NotificationSettingService;
import com.barlow.core.enumerate.LegislationType;

/**
 * 추가 예정
 * - 계정 정보 (로그인 정보 및 로그인 기능)
 * - 스크랩 정보 등등
 */
@Service
public class MenuFacade {

	private final MenuService menuService;
	private final NotificationSettingService notificationSettingService;

	public MenuFacade(MenuService menuService, NotificationSettingService notificationSettingService) {
		this.menuService = menuService;
		this.notificationSettingService = notificationSettingService;
	}

	public List<NotificationSetting> retrieveNotificationSettingMenu(User user) {
		return menuService.retrieveNotificationSettingMenu(user);
	}

	public void activateNotify(LegislationType type, User user) {
		notificationSettingService.activateSetting(type, user);
	}

	public void deactivateNotify(LegislationType type, User user) {
		notificationSettingService.deactivateSetting(type, user);
	}
}
