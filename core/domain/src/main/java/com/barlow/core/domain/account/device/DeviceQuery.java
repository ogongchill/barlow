package com.barlow.core.domain.account.device;

import com.barlow.core.enumerate.DeviceOs;

public record DeviceQuery(String deviceId, DeviceOs deviceOs) {
}
