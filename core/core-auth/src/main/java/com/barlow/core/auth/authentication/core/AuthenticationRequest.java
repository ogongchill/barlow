package com.barlow.core.auth.authentication.core;

public class AuthenticationRequest<T extends Credential> {

    private final T credentials;

    public AuthenticationRequest(T credentials) {
        this.credentials = credentials;
    }

    public CredentialType getAuthenticationType() {
        return credentials.getAuthenticationType();
    }

    public T getCredentials() {
        return credentials;
    }
}
