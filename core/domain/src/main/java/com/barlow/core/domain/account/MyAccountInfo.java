package com.barlow.core.domain.account;

import com.barlow.core.domain.externalauth.ExternalPrincipal;
import com.barlow.core.domain.device.Device;

import java.util.List;

public record MyAccountInfo(AccountProfile profile, List<ExternalPrincipal> authProviders, List<Device> devices) {
}
