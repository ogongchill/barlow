package com.barlow.services.auth.authentication.oauth;

import com.barlow.core.enumerate.AuthProvider;

public record OidcAuthenticationRequest(
        AuthProvider authProvider,
        String idToken
) {
}
