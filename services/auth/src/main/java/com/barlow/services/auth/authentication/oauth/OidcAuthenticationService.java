package com.barlow.services.auth.authentication.oauth;

import com.barlow.core.domain.registration.ExternalPrincipal;
import com.barlow.core.enumerate.AuthProvider;
import com.barlow.services.auth.authentication.core.AuthenticationException;
import com.barlow.services.auth.authentication.core.AuthenticationExceptionType;
import org.springframework.stereotype.Component;

@Component
public class OidcAuthenticationService {

    private final KakaoOidcAuthenticator kakaoOidcAuthenticator;

    public OidcAuthenticationService(KakaoOidcAuthenticator kakaoOidcAuthenticator) {
        this.kakaoOidcAuthenticator = kakaoOidcAuthenticator;
    }

    public ExternalPrincipal authenticate(OidcAuthenticationRequest oidcRequest) {
        if(oidcRequest.authProvider() == AuthProvider.KAKAO) {
            OidcPrincipal principal = kakaoOidcAuthenticator.authenticate(new KakaoIdToken(oidcRequest.idToken()));
            return new ExternalPrincipal(AuthProvider.ofIssuer(principal.getIss()), principal.getSub());
        }
        throw new AuthenticationException(oidcRequest.authProvider().name() + "은 지원되지 않는 AuthProvider입니다.", AuthenticationExceptionType.INVALID_CREDENTIAL);
    }
}
