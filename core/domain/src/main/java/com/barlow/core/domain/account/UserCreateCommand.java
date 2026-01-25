package com.barlow.core.domain.account;

import com.barlow.core.domain.User;
import com.barlow.core.enumerate.DeviceOs;

public record UserCreateCommand(
		DeviceOs os,
		String deviceId,
		String deviceToken,
		String nickname,
		User.Role role
) {

	public static UserCreateCommand ofGuest(
			DeviceOs os,
			String deviceId,
			String deviceToken,
			String nickname
	) {
		return new UserCreateCommand(
				os,
				deviceId,
				deviceToken,
				nickname,
				User.Role.GUEST
		);
	}

	public static UserCreateCommand ofMember(
			DeviceOs os,
			String deviceId,
			String deviceToken,
			String nickname
	) {
		return new UserCreateCommand(
				os,
				deviceId,
				deviceToken,
				nickname,
				User.Role.MEMBER
		);
	}

	DeviceQuery toDeviceQuery() {
		return new DeviceQuery(deviceId, os);
	}

	UserRegisterCommand toUserRegisterCommand() {
		return new UserRegisterCommand(nickname, role);
	}

	DeviceRegisterCommand toDeviceCommand(long userNo) {
		return new DeviceRegisterCommand(deviceId, os, deviceToken, userNo);
	}
}
