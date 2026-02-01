package com.barlow.core.domain.account.authprovider;

import com.barlow.core.enumerate.AuthProvider;

public record ExternalPrincipal(
        AuthProvider authProvider,
        String sub
) {
}
