package com.barlow.core.auth.authentication.core;

public interface Authenticator<C extends Credential, P extends Principal> {

    AuthenticationResult<P> authenticate(AuthenticationRequest<C> request);
}
