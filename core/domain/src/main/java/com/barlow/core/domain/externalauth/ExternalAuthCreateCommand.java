package com.barlow.core.domain.externalauth;

import com.barlow.core.enumerate.AuthProvider;

public record ExternalAuthCreateCommand(AuthProvider authProvider, String sub, Long userNo) {
	public static ExternalAuthCreateCommand from(ExternalPrincipal externalPrincipal, Long userNo) {
		return new ExternalAuthCreateCommand(externalPrincipal.authProvider(), externalPrincipal.sub(), userNo);
	}
}
