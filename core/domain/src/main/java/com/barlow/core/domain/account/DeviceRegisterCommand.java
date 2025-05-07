package com.barlow.core.domain.account;

import com.barlow.core.enumerate.DeviceOs;

public record DeviceRegisterCommand(
	String deviceId,
	DeviceOs os,
	String deviceToken,
	long userNo
) {
}
