package com.barlow.core.domain.account;

import static com.barlow.core.exception.CoreDomainExceptionType.CONFLICT_EXCEPTION;

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
		if (deviceRepository.readOrNull(command.toDeviceQuery()) != null) {
			throw new AccountDomainException(CONFLICT_EXCEPTION, "해당 디바이스로 등록된 회원이 이미 존재함");
		}
		User user = userRepository.create(command.toUserCommand());
		deviceRepository.save(command.toDeviceCommand(user.getUserNo()));
		return user;
	}
}
