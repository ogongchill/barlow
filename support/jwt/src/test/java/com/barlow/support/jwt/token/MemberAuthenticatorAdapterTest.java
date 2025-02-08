package com.barlow.support.jwt.token;

import com.auth0.jwt.JWT;

import com.barlow.core.auth.authentication.token.Token;
import com.barlow.support.jwt.crypto.PrivateKeyAlgorithm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = MemberAuthenticatorAdapter.class)
class MemberAuthenticatorAdapterTest extends JwtTest {

    @Autowired
    MemberAuthenticatorAdapter memberAuthenticatorAdapter;
    @Autowired
    PrivateKeyAlgorithm privateKeyAlgorithm;

    @Test
    void authenticate() {
        String jwt = JWT.create()
                .withIssuer("barlow")
                .withClaim("memberNo", 1)
                .withClaim("role", "GUEST")
                .sign(privateKeyAlgorithm.getAlgorithm());
        memberAuthenticatorAdapter.validate(new Token(jwt));
    }
}