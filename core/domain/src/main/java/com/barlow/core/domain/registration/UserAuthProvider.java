package com.barlow.core.domain.registration;

import com.barlow.core.enumerate.AuthProvider;

import java.util.List;

public record UserAuthProvider(
        Long userNo,
        List<ExternalPrincipal> externalPrincipals
) {

    public boolean has(AuthProvider authProvider) {
        return externalPrincipals.stream()
                .anyMatch(externalPrincipal -> externalPrincipal.authProvider().equals(authProvider));
    }

    public UserAuthProviderCreateCommand toCommand(ExternalPrincipal externalPrincipal) {
        if(has(externalPrincipal.authProvider())) {
            throw RegistrationException.authProviderExists(externalPrincipal.authProvider());
        }
        return UserAuthProviderCreateCommand.from(externalPrincipal, userNo);
    }
}
