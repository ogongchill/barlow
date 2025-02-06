package com.barlow.core.auth.jwt;

public enum ReservedClaims {
    ISSUER("iss"),
    EXPIRATION("exp")
    ;

    private final String value;

    public String getValue() {
        return value;
    }

    ReservedClaims(String value) {
        this.value = value;
    }
}
