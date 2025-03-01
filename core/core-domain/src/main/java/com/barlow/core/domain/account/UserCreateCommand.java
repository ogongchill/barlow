package com.barlow.core.domain.account;

import com.barlow.core.enumerate.DeviceOs;

public record UserCreateCommand(
	DeviceOs os,
	String deviceId,
	String deviceToken,
	String nickname
) {
	DeviceRegisterCommand toDeviceCommand(long userNo) {
		return new DeviceRegisterCommand(deviceId, os, deviceToken, userNo);
	}
}
