package com.barlow.core.domain.account;

import static com.barlow.core.exception.CoreDomainExceptionType.CONFLICT_EXCEPTION;

import org.springframework.stereotype.Service;

import com.barlow.core.domain.User;
import com.barlow.core.domain.notificationsetting.NotificationSettingActivator;

@Service
public class AccountCreateService {

	private final DeviceReader deviceReader;
	private final UserCreator userCreator;
	private final NotificationSettingActivator notificationSettingActivator;

	public AccountCreateService(
		DeviceReader deviceReader,
		UserCreator userCreator,
		NotificationSettingActivator notificationSettingActivator
	) {
		this.deviceReader = deviceReader;
		this.userCreator = userCreator;
		this.notificationSettingActivator = notificationSettingActivator;
	}

	public User createGuest(UserCreateCommand command) {
		if (deviceReader.read(command.toDeviceQuery()) != null) {
			throw new AccountDomainException(CONFLICT_EXCEPTION, "해당 디바이스로 등록된 회원이 이미 존재함");
		}
		User user = userCreator.create(command);
		notificationSettingActivator.activateDefault(user);
		return user;
	}
}
