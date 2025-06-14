package com.barlow.core.domain.version;

import org.springframework.stereotype.Component;

import com.barlow.core.enumerate.ClientVersionStatus;

@Component
public class AllowOnlyOfficialReleaseStrategy implements ClientVersionUpdateStrategy {

	@Override
	public ClientVersionStatus evaluate(SemanticVersion clientVersion, AvailableClientVersion availableClientVersion) {
		if (clientVersion.isOfficialRelease()) {
			return ClientVersionStatus.INVALID;
		}
		if (clientVersion.isLessThan(availableClientVersion.minimumClientVersion())) {
			return ClientVersionStatus.NEED_FORCE_UPDATE;
		}
		if (clientVersion.isLessThan(availableClientVersion.latestClientVersion())) {
			return ClientVersionStatus.UPDATE_AVAILABLE;
		}
		return ClientVersionStatus.LATEST;
	}
}
