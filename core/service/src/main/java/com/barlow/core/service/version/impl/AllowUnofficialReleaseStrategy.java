package com.barlow.core.service.version.impl;

import com.barlow.core.domain.version.AvailableClientVersion;
import com.barlow.core.domain.version.ClientVersionUpdateStrategy;
import com.barlow.core.domain.version.SemanticVersion;
import com.barlow.core.enumerate.ClientVersionStatus;

import org.springframework.stereotype.Component;

@Component
public class AllowUnofficialReleaseStrategy implements ClientVersionUpdateStrategy {

	@Override
	public ClientVersionStatus evaluate(SemanticVersion clientVersion, AvailableClientVersion availableClientVersion) {
		if (clientVersion.isLessThan(availableClientVersion.minimumClientVersion())) {
			return ClientVersionStatus.NEED_FORCE_UPDATE;
		}
		if (clientVersion.isLessThan(availableClientVersion.latestClientVersion())) {
			return ClientVersionStatus.UPDATE_AVAILABLE;
		}
		return ClientVersionStatus.LATEST;
	}
}
