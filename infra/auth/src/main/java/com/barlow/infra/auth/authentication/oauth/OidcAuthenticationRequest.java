package com.barlow.infra.auth.authentication.oauth;

import com.barlow.core.enumerate.AuthProvider;

public record OidcAuthenticationRequest(AuthProvider authProvider, String idToken) {
}
