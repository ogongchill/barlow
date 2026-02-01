package com.barlow.core.domain.account.login;

import com.barlow.core.domain.account.authprovider.ExternalPrincipal;
import com.barlow.core.domain.account.device.DeviceQuery;
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
