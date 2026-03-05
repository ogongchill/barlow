package com.barlow.core.domain.account;

import com.barlow.core.domain.device.DeviceQuery;
import com.barlow.core.enumerate.DeviceOs;

public record LoginCommand(String deviceId, DeviceOs deviceOs, String deviceToken) {
	public DeviceQuery toDeviceQuery() {
		return new DeviceQuery(deviceId, deviceOs);
	}
}
