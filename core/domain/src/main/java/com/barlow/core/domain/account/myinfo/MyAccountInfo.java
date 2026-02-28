package com.barlow.core.domain.account.myinfo;

import com.barlow.core.domain.account.authprovider.ExternalPrincipal;
import com.barlow.core.domain.account.device.Device;

import java.util.List;

public record MyAccountInfo(
	AccountProfile profile,
	List<ExternalPrincipal> authProviders,
	List<Device> devices
) {
}
