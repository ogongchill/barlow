package com.barlow.core.domain.account.create;

import com.barlow.core.domain.account.AccountDomainException;
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
		if(command.role() != User.Role.GUEST) {
			throw AccountDomainException.modificationException("Guest User 요청이 아닌 상태로 User를 생성 할 수 없습니다.");
		}
		User user = userCreator.create(command);
		notificationSettingActivator.activateDefault(user);
		return user;
	}
}
