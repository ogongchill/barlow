package com.barlow.core.domain.version;

import com.barlow.core.enumerate.DeviceOs;

public record ClientVersionQuery(DeviceOs deviceOs, String clientVersion) {
}
