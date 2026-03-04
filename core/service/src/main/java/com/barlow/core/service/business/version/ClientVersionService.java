package com.barlow.core.service.business.version;

import com.barlow.core.domain.version.AvailableClientVersion;
import com.barlow.core.domain.version.ClientVersionQuery;
import com.barlow.core.domain.version.ClientVersionRepository;
import com.barlow.core.domain.version.ClientVersionUpdateStrategy;
import com.barlow.core.domain.version.SemanticVersion;
import com.barlow.core.enumerate.ClientVersionStatus;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class ClientVersionService {

	private final ClientVersionRepository clientVersionRepository;
	private final ClientVersionUpdateStrategy clientVersionUpdateStrategy;

	public ClientVersionService(
		ClientVersionRepository clientVersionRepository,
		@Qualifier("allowUnofficialReleaseStrategy") ClientVersionUpdateStrategy clientVersionUpdateStrategy
	) {
		this.clientVersionRepository = clientVersionRepository;
		this.clientVersionUpdateStrategy = clientVersionUpdateStrategy;
	}

	public ClientVersionStatus checkClientVersion(ClientVersionQuery query) {
		SemanticVersion targetClientVersion = SemanticVersion.of(query.clientVersion());
		AvailableClientVersion currentAvailableVersion = clientVersionRepository.retrieveByDeviceOs(query.deviceOs());
		return clientVersionUpdateStrategy.evaluate(targetClientVersion, currentAvailableVersion);
	}
}
