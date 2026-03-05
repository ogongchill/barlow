package com.barlow.core.service.account.business;

import com.barlow.core.domain.User;
import com.barlow.core.domain.device.Device;
import com.barlow.core.domain.account.LoginCommand;
import com.barlow.core.domain.account.MemberLoginCommand;
import com.barlow.core.service.account.impl.DeviceRefresher;
import com.barlow.core.service.account.impl.UserReader;

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
