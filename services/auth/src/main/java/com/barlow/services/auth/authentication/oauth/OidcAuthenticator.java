package com.barlow.services.auth.authentication.oauth;

import com.barlow.services.auth.authentication.core.Authenticator;

public interface OidcAuthenticator<T extends IdToken> extends Authenticator<T, OidcPrincipal> {
}

