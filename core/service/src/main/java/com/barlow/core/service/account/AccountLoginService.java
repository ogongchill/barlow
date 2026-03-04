package com.barlow.core.service.account;

import com.barlow.core.domain.User;
import com.barlow.core.domain.account.device.Device;
import com.barlow.core.domain.account.login.LoginCommand;
import com.barlow.core.domain.account.login.MemberLoginCommand;

import org.springframework.stereotype.Service;

@Service
public class AccountLoginService {

	private final DeviceRefresher deviceRefresher;
	private final UserReader userReader;

	public AccountLoginService(DeviceRefresher deviceRefresher, UserReader userReader) {
		this.deviceRefresher = deviceRefresher;
		this.userReader = userReader;
	}

	public User guestLogin(LoginCommand command) {
		Device device = deviceRefresher.refresh(command.toDeviceQuery(), command.deviceToken());
		return userReader.read(device.getUserNo());
	}

	public User memberLogin(MemberLoginCommand command) {
		User user = userReader.read(command.externalPrincipal());
		deviceRefresher.refresh(command.toDeviceQuery(), command.deviceToken());
		return user;
	}
}
