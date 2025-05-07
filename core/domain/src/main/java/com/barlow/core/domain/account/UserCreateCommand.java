package com.barlow.core.domain.account;

import com.barlow.core.enumerate.DeviceOs;

public record UserCreateCommand(
	DeviceOs os,
	String deviceId,
	String deviceToken,
	String nickname
) {
	DeviceQuery toDeviceQuery() {
		return new DeviceQuery(deviceId, os);
	}

	UserRegisterCommand toUserCommand() {
		return new UserRegisterCommand(nickname);
	}

	DeviceRegisterCommand toDeviceCommand(long userNo) {
		return new DeviceRegisterCommand(deviceId, os, deviceToken, userNo);
	}
}
