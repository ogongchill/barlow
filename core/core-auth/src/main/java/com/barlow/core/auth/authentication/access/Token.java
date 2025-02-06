package com.barlow.core.auth.authentication.access;

import com.barlow.core.auth.authentication.core.CredentialType;
import com.barlow.core.auth.authentication.core.Credential;

public class Token extends Credential {

    private final String value;

    public Token(String token) {
        super(CredentialType.TOKEN);
        this.value = token;
    }

    public String getValue() {
        return value;
    }
}
