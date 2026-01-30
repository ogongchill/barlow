package com.barlow.core.enumerate;

import com.barlow.core.domain.registration.RegistrationException;

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
        AuthProvider provider =  ISSUER_MAP.get(issuer);
        if(provider == null) {
            throw RegistrationException.externalAuthenticationException(issuer + "는 지원되지 않는 외부 인증입니다.");
        }
        return provider;
    }
}
