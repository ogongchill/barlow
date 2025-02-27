package com.barlow.core.enumerate;

public enum DeviceOs {
	IOS, ANDROID,
	;

	public boolean isIOS() {
		return this.equals(IOS);
	}

	public boolean isANDROID() {
		return this.equals(ANDROID);
	}
}
