package com.barlow.app.api.controller.v1.auth;

import com.barlow.app.support.error.CoreApiException;
import com.barlow.core.enumerate.AuthProvider;

public record OidcRequest(
        String authProvider,
        String idToken
) {
    public AuthProvider toAuthProvider() {
        if(authProvider.equals("KAKAO")  || authProvider.equals("kakao")) {
            return AuthProvider.KAKAO;
        }
        throw CoreApiException.badRequest(authProvider + "는 지원하지 않습니다.");
    }
}
