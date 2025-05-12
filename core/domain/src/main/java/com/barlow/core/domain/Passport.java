package com.barlow.core.domain;

import com.barlow.core.enumerate.DeviceOs;

public final class Passport {

	private final User user;
	private final Device device;

	private Passport(User user, Device device) {
		this.user = user;
		this.device = device;
	}

	public boolean isAtLeastGuest() {
		return user.isGuestUser() || user.isMemberUser() || user.isAdminUser();
	}

	public boolean isForGuest() {
		return user.isGuestUser();
	}

	public boolean isValidOs() {
		return device.os.isIOS() || device.os.isANDROID();
	}

	public static Passport create(User user, String deviceId, String osVersion, String os) {
		return new Passport(user, new Device(deviceId, osVersion, DeviceOs.valueOf(os.toUpperCase())));
	}

	public User getUser() {
		return user;
	}

	record Device(
		String deviceId,
		String osVersion,
		DeviceOs os
	) {
	}
}
