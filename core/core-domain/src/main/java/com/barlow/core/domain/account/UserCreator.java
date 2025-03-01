package com.barlow.core.domain.account;

import org.springframework.stereotype.Component;

import com.barlow.core.domain.User;

@Component
public class UserCreator {

	private final UserRepository userRepository;
	private final DeviceRepository deviceRepository;

	public UserCreator(UserRepository userRepository, DeviceRepository deviceRepository) {
		this.userRepository = userRepository;
		this.deviceRepository = deviceRepository;
	}

	public User create(UserCreateCommand command) {
		User user = userRepository.create(command);
		deviceRepository.save(command.toDeviceCommand(user.getUserNo()));
		return user;
	}
}
