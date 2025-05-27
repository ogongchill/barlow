package com.barlow.core.domain.version;

import com.barlow.core.enumerate.DeviceOs;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientVersionRepository {

    AvailableClientVersion retrieveByDeviceOs(DeviceOs deviceOs);
}
