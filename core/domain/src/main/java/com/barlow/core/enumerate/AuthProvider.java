package com.barlow.core.enumerate;

public enum AuthProvider {

    KAKAO("https://kauth.kakao.com"),
    NAVER("https://nid.naver.com")
    ;

    private final String iss;

    AuthProvider(String iss) {
        this.iss = iss;
    }

    public String getIssuer() {
        return iss;
    }
}
