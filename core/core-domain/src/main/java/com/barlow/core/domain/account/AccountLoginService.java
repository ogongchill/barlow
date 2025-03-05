package com.barlow.core.domain.account;

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
}
