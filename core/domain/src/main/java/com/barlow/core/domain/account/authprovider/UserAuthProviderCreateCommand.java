package com.barlow.core.domain.account.authprovider;

import com.barlow.core.enumerate.AuthProvider;

public record UserAuthProviderCreateCommand(AuthProvider authProvider, String sub, Long userNo) {
	public static UserAuthProviderCreateCommand from(ExternalPrincipal externalPrincipal, Long userNo) {
		return new UserAuthProviderCreateCommand(externalPrincipal.authProvider(), externalPrincipal.sub(), userNo);
	}
}
