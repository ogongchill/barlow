package com.barlow.infra.auth.authentication.oauth;

import com.barlow.infra.auth.authentication.core.Authenticator;

public interface OidcAuthenticator<T extends IdToken> extends Authenticator<T, OidcPrincipal> {}
