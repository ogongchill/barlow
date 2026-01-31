package com.barlow.core.enumerate;

import java.util.Map;

public enum AuthProvider {

    KAKAO("https://kauth.kakao.com"),
    NAVER("https://nid.naver.com")
    ;

    private static final Map<String, AuthProvider> ISSUER_MAP = Map.of(
            KAKAO.iss, KAKAO,
            NAVER.iss, NAVER
    );

    private final String iss;

    AuthProvider(String iss) {
        this.iss = iss;
    }

    public String getIssuer() {
        return iss;
    }

    public static AuthProvider ofIssuer(String issuer) {
        return ISSUER_MAP.get(issuer);
    }
}
