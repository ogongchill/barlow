package com.barlow.core.domain.account;

import org.springframework.stereotype.Service;

import com.barlow.core.domain.User;

@Service
public class AccountLoginService {

	private final UserReader userReader;
	private final DeviceReader deviceReader;
	private final DeviceRepository deviceRepository;

	public AccountLoginService(UserReader userReader, DeviceReader deviceReader, DeviceRepository deviceRepository) {
		this.userReader = userReader;
		this.deviceReader = deviceReader;
		this.deviceRepository = deviceRepository;
	}

	public User guestLogin(LoginCommand command) {
		Device device = deviceReader.read(command.toDeviceQuery());
		if (device.isChanged(command.deviceToken())) {
			Device modifiedDevice = device.modifyToken(command.deviceToken());
			deviceRepository.update(modifiedDevice);
		}
		return userReader.read(device.getUserNo());
	}
}
