package com.barlow.core.domain.account;

import com.barlow.core.enumerate.DeviceOs;

public class Device {

	private final long userNo;
	private final String deviceId;
	private final DeviceOs deviceOs;
	private final String deviceToken;
	private final Status status;

	public Device(long userNo, String deviceId, DeviceOs deviceOs, String deviceToken, Status status) {
		this.userNo = userNo;
		this.deviceId = deviceId;
		this.deviceOs = deviceOs;
		this.deviceToken = deviceToken;
		this.status = status;
	}

	boolean isInactive() {
		return status == Status.INACTIVE;
	}

	boolean isChanged(String token) {
		return !deviceToken.equals(token);
	}

	Device modifyToken(String newToken) {
		return new Device(userNo, deviceId, deviceOs, newToken, status);
	}

	long getUserNo() {
		return userNo;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public String getDeviceToken() {
		return deviceToken;
	}

	public enum Status {
		ACTIVE, INACTIVE;
	}
}
