package com.barlow.core.domain.account;

import com.barlow.core.domain.externalauth.ExternalPrincipal;
import com.barlow.core.domain.device.DeviceQuery;
import com.barlow.core.enumerate.DeviceOs;

public record MemberLoginCommand(String deviceId, DeviceOs deviceOs, String deviceToken,
	ExternalPrincipal externalPrincipal) {
	public DeviceQuery toDeviceQuery() {
		return new DeviceQuery(deviceId, deviceOs);
	}
}
