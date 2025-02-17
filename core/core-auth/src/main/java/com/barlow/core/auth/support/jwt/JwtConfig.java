package com.barlow.core.auth.support.jwt;


import java.time.Duration;

public class JwtConfig {

    private final String issuer;
    private final Duration tokenLifeTime;

    public JwtConfig(String issuer, Duration tokenLifeTime) {
        this.issuer = issuer;
        this.tokenLifeTime = tokenLifeTime;
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
