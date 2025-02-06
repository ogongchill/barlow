package com.barlow.support.jwt.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.barlow.core.auth.authorization.AuthorizableMemberInfo;
import com.barlow.support.jwt.crypto.PrivateKeyAlgorithm;
import com.barlow.support.jwt.crypto.PublicKeyAlgorithm;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

@Component
public class MemberTokenProvider {

    private final PrivateKeyAlgorithm privateKeyAlgorithm;
    private final PublicKeyAlgorithm publicKeyAlgorithm;
    private final JwtConfig.Issuer issuer;

    public MemberTokenProvider(
            PrivateKeyAlgorithm privateKeyAlgorithm,
            PublicKeyAlgorithm publicKeyAlgorithm,
            JwtConfig.Issuer issuer) {
        this.privateKeyAlgorithm = privateKeyAlgorithm;
        this.publicKeyAlgorithm = publicKeyAlgorithm;
        this.issuer = issuer;
    }

    public String sign(AuthorizableMemberInfo memberInfo) {
        return JWT.create()
                .withSubject(memberInfo.memberNo())
                .withIssuedAt(Date.from(Instant.now()))
                .withIssuer(issuer.name())
                .withClaim("role", memberInfo.role())
                .sign(privateKeyAlgorithm.getAlgorithm());
    }

    public String getPayload(String token) {
        JWTVerifier verifier = JWT.require(publicKeyAlgorithm.getAlgorithm())
                .withIssuer(issuer.name())
                .build();
        String encodedPayload = verifier.verify(token).getPayload();
        byte[] decodedBytes = Base64.getUrlDecoder().decode(encodedPayload);
        return new String(decodedBytes, StandardCharsets.UTF_8);
    }
}
