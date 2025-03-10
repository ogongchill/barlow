package com.barlow.core.domain.account;

import org.springframework.stereotype.Service;

import com.barlow.core.domain.User;
import com.barlow.core.domain.notificationsetting.NotificationSettingActivator;

@Service
public class AccountCreateService {

	private final UserCreator userCreator;
	private final NotificationSettingActivator notificationSettingActivator;

	public AccountCreateService(
		UserCreator userCreator,
		NotificationSettingActivator notificationSettingActivator
	) {
		this.userCreator = userCreator;
		this.notificationSettingActivator = notificationSettingActivator;
	}

	public User createGuest(UserCreateCommand command) {
		User user = userCreator.create(command);
		notificationSettingActivator.activateDefault(user);
		return user;
	}
}
