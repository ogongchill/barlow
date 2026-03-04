package com.barlow.core.domain.account.authprovider;

import com.barlow.core.enumerate.AuthProvider;

public record ProviderAndSubQuery(AuthProvider authProvider, String sub) {
	public static ProviderAndSubQuery from(ExternalPrincipal principal) {
		return new ProviderAndSubQuery(principal.authProvider(), principal.sub());
	}
}
