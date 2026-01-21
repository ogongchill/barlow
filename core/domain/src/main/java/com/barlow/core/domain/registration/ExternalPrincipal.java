package com.barlow.core.domain.registration;

import com.barlow.core.enumerate.AuthProvider;

public record ExternalPrincipal(
        AuthProvider authProvider,
        String sub
) {
}
