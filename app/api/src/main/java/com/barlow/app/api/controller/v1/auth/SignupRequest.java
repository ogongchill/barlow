package com.barlow.app.api.controller.v1.auth;

import com.barlow.core.domain.account.UserCreateCommand;
import com.barlow.core.enumerate.DeviceOs;
import com.barlow.app.support.validate.Validatable;

public record SignupRequest(
	String deviceOs,
	String deviceId,
	String deviceToken,
	String nickname
) implements Validatable {

	UserCreateCommand toCommand() {
		return new UserCreateCommand(
			DeviceOs.valueOf(deviceOs.toUpperCase()),
			deviceId,
			deviceToken,
			nickname
		);
	}

	@Override
	public void validate() {
		if (deviceId == null || deviceId.isBlank()) {
			throw new IllegalArgumentException("Device ID cannot be null or empty");
		}
		if (deviceToken == null || deviceToken.isBlank()) {
			throw new IllegalArgumentException("Device token cannot be null or empty");
		}
		if (deviceOs == null || deviceOs.isBlank()) {
			throw new IllegalArgumentException("Device Os cannot be null or empty");
		}
		if (nickname == null || nickname.isBlank()) {
			throw new IllegalArgumentException("Device Os cannot be null or empty");
		}
		if (!deviceOs.matches("^(?i)(ios|android)$")) {
			throw new IllegalArgumentException("os 는 대소문자 관계 없이 'ios' 와 'android' 타입만 허용됨");
		}
	}
}
