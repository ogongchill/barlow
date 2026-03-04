package com.barlow.infra.auth.authentication.oauth;

import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.JwkProviderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Configuration
public class OauthConfig {

    @Bean
    public JwkProvider kakaoJwkProvider(@Value("${auth.oidc.kakao.jwk-uri}") String kakaoJwksUri) {
        return new JwkProviderBuilder(kakaoJwksUri)
                .cached(10, 24, TimeUnit.HOURS)
                .rateLimited(10, 1, TimeUnit.MINUTES) // 분당 10번만 jwk 재조회 허용
                .build();
    }

    @Bean
    public Map<String, String> kakaoOidcClaims(
            @Value("${auth.oidc.kakao.iss}") String iss,
            @Value("${auth.oidc.kakao.aud}") String aud
    ) {
        HashMap<String, String> claims = new HashMap<>();
        claims.put("iss", iss);
        claims.put("aud", aud);
        return claims;
    }
}
