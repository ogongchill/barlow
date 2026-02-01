package com.barlow.core.domain.account.login;

import com.barlow.core.domain.account.device.Device;
import com.barlow.core.domain.account.device.DeviceRefresher;
import org.springframework.stereotype.Service;

import com.barlow.core.domain.User;

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
