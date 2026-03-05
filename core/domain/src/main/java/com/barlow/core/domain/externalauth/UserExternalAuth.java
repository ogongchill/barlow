package com.barlow.core.domain.externalauth;

import com.barlow.core.domain.account.RegistrationException;
import com.barlow.core.enumerate.AuthProvider;

import java.util.List;

public class UserExternalAuth {

	private final Long userNo;
	private final List<ExternalPrincipal> externalPrincipals;

	public UserExternalAuth(Long userNo, List<ExternalPrincipal> externalPrincipals) {
		this.userNo = userNo;
		this.externalPrincipals = List.copyOf(externalPrincipals);
	}

	public boolean has(AuthProvider authProvider) {
		return externalPrincipals.stream()
			.anyMatch(externalPrincipal -> externalPrincipal.authProvider().equals(authProvider));
	}

	public ExternalAuthCreateCommand toCommand(ExternalPrincipal externalPrincipal) {
		if (has(externalPrincipal.authProvider())) {
			throw RegistrationException.authProviderExists(externalPrincipal.authProvider());
		}
		return ExternalAuthCreateCommand.from(externalPrincipal, userNo);
	}

	public Long getUserNo() {
		return userNo;
	}

	public List<ExternalPrincipal> getExternalPrincipals() {
		return externalPrincipals;
	}
}