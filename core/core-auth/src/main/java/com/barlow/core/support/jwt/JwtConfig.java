package com.barlow.core.support.jwt;


import java.time.Duration;

public class JwtConfig {

    private final String issuer;
    private final Duration tokenLifeTime;

    public JwtConfig(String issuer, Duration tokenLifeTime) {
        this.issuer = issuer;
        this.tokenLifeTime = tokenLifeTime;
    }

    public String getIssuer() {
        return issuer;
    }

    public Duration getTokenLifeTime() {
        return tokenLifeTime;
    }

    public enum Claims {

        ISSUER("iss"),
        ROLE("role"),
        MEMBER_NO("memberNo"),
        EXPIRATION("exp"),
        ISSUED_AT("iat")
        ;

        private final String name;

        Claims(String claimName) {
            this.name = claimName;
        }

        public String getName() {
            return name;
        }
    }
}
