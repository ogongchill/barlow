package com.barlow.core.domain.externalauth;

import com.barlow.core.enumerate.AuthProvider;

public record ExternalSubQuery(AuthProvider authProvider, String sub) {
	public static ExternalSubQuery from(ExternalPrincipal principal) {
		return new ExternalSubQuery(principal.authProvider(), principal.sub());
	}
}
