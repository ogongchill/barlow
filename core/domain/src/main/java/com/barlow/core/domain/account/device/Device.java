package com.barlow.core.domain.account.device;

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

	public boolean isInactive() {
		return status == Status.INACTIVE;
	}

	public boolean isChanged(String token) {
		return !deviceToken.equals(token);
	}

	public Device modifyToken(String newToken) {
		return new Device(userNo, deviceId, deviceOs, newToken, status);
	}

	public long getUserNo() {
		return userNo;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public DeviceOs getDeviceOs() {
		return deviceOs;
	}

	public String getDeviceToken() {
		return deviceToken;
	}

	public Status getStatus() {
		return status;
	}

	public enum Status {
		ACTIVE, INACTIVE;
	}
}
