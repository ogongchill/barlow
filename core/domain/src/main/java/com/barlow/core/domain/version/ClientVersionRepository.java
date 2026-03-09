package com.barlow.core.domain.version;

import com.barlow.core.enumerate.DeviceOs;

public interface ClientVersionRepository {
	ClientVersionPolicy retrieveByDeviceOs(DeviceOs deviceOs);
}
