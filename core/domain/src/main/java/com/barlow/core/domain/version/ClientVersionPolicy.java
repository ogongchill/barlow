package com.barlow.core.domain.version;

import com.barlow.core.enumerate.ClientVersionStatus;
import com.barlow.core.enumerate.DeviceOs;

public class ClientVersionPolicy {

	private final DeviceOs deviceOs;
	private final SemanticVersion minimumClientVersion;
	private final SemanticVersion latestClientVersion;

	private ClientVersionPolicy(DeviceOs deviceOs, SemanticVersion minimumClientVersion,
		SemanticVersion latestClientVersion) {
		this.deviceOs = deviceOs;
		this.minimumClientVersion = minimumClientVersion;
		this.latestClientVersion = latestClientVersion;
	}

	public static ClientVersionPolicy of(DeviceOs deviceOs, SemanticVersion minimumClientVersion,
		SemanticVersion latestClientVersion) {
		return new ClientVersionPolicy(deviceOs, minimumClientVersion, latestClientVersion);
	}

	public ClientVersionStatus evaluate(SemanticVersion clientVersion) {
		if (clientVersion.isLessThan(minimumClientVersion)) {
			return ClientVersionStatus.NEED_FORCE_UPDATE;
		}
		if (clientVersion.isLessThan(latestClientVersion)) {
			return ClientVersionStatus.UPDATE_AVAILABLE;
		}
		return ClientVersionStatus.LATEST;
	}

	public DeviceOs getDeviceOs() {
		return deviceOs;
	}

	public SemanticVersion getMinimumClientVersion() {
		return minimumClientVersion;
	}

	public SemanticVersion getLatestClientVersion() {
		return latestClientVersion;
	}
}
