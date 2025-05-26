package com.barlow.core.domain.version;

import com.barlow.core.enumerate.ClientVersionStatus;
import org.springframework.stereotype.Service;

@Service
public class ClientVersionService {

    private final ClientVersionRepository clientVersionRepository;
    private final ClientVersionUpdateStrategy clientVersionUpdateStrategy;

    public ClientVersionService(ClientVersionRepository clientVersionRepository, ClientVersionUpdateStrategy clientVersionUpdateStrategy) {
        this.clientVersionRepository = clientVersionRepository;
        this.clientVersionUpdateStrategy = clientVersionUpdateStrategy;
    }

    public ClientVersionStatus checkClientVersion(ClientVersionQuery query) {
        SemanticVersion targetClientVersion = SemanticVersion.of(query.clientVersion());
        AvailableClientVersion currentAvailableVersion = clientVersionRepository.retrieveByDeviceOs(query.deviceOs());
        return clientVersionUpdateStrategy.evaluate(targetClientVersion, currentAvailableVersion);
    }
}
