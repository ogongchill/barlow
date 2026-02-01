package com.barlow.core.domain.account;

import com.barlow.core.domain.registration.ExternalPrincipal;
import com.barlow.core.enumerate.DeviceOs;

public record MemberLoginCommand(
    String deviceId,
    DeviceOs deviceOs,
    String deviceToken,
    ExternalPrincipal externalPrincipal
) {
    DeviceQuery toDeviceQuery() {
        return new DeviceQuery(deviceId, deviceOs);
    }
}
