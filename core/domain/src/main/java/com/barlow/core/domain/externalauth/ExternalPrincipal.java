package com.barlow.core.domain.externalauth;

import com.barlow.core.enumerate.AuthProvider;

public record ExternalPrincipal(AuthProvider authProvider, String sub) {
}
