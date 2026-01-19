package com.barlow.services.auth.authentication.oauth;

import com.barlow.services.auth.authentication.core.Credential;
import com.barlow.services.auth.authentication.core.CredentialType;

public class IdToken extends Credential {

    private final String value;

    public IdToken(CredentialType authenticationType, String value) {
        super(authenticationType);
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
