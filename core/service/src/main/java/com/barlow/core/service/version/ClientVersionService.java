package com.barlow.core.service.version;

import com.barlow.core.domain.version.ClientVersionPolicy;
import com.barlow.core.domain.version.ClientVersionQuery;
import com.barlow.core.domain.version.ClientVersionRepository;
import com.barlow.core.domain.version.SemanticVersion;
import com.barlow.core.enumerate.ClientVersionStatus;

import org.springframework.stereotype.Service;

@Service
public class ClientVersionService {

	private final ClientVersionRepository clientVersionRepository;

	public ClientVersionService(ClientVersionRepository clientVersionRepository) {
		this.clientVersionRepository = clientVersionRepository;
	}

	public ClientVersionStatus checkClientVersion(ClientVersionQuery query) {
		SemanticVersion clientVersion = SemanticVersion.of(query.clientVersion());
		ClientVersionPolicy available = clientVersionRepository.retrieveByDeviceOs(query.deviceOs());
		return available.evaluate(clientVersion);
	}
}
