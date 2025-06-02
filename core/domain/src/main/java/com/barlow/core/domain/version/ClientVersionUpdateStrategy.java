package com.barlow.core.domain.version;

import com.barlow.core.enumerate.ClientVersionStatus;

public interface ClientVersionUpdateStrategy {
	ClientVersionStatus evaluate(SemanticVersion clientVersion, AvailableClientVersion availableClientVersion);
}
