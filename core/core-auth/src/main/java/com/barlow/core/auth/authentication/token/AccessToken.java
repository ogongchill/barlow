package com.barlow.core.auth.authentication.token;

import com.barlow.core.auth.authentication.core.Credential;
import com.barlow.core.auth.authentication.core.CredentialType;

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
