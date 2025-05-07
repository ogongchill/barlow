package com.barlow.services.auth.authentication.token;

import com.barlow.services.auth.authentication.core.Credential;
import com.barlow.services.auth.authentication.core.CredentialType;

public class AccessToken extends Credential {

    private final String value;

    public AccessToken(String token) {
        super(CredentialType.TOKEN);
        this.value = token;
    }

    public String getValue() {
        return value;
    }
}
