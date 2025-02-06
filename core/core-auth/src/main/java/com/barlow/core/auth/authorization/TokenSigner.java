package com.barlow.core.auth.authorization;

import com.auth0.jwt.JWT;
import com.barlow.core.auth.crypto.PrivateKeyAlgorithm;
import com.barlow.core.auth.jwt.Payload;

public class TokenSigner {

    private final PrivateKeyAlgorithm privateKeyAlgorithm;

    public TokenSigner(PrivateKeyAlgorithm privateKeyAlgorithm) {
        this.privateKeyAlgorithm = privateKeyAlgorithm;
    }

    public String sign(Payload payload) {
        return JWT.create()
                .withPayload(payload.toJsonString())
                .sign(privateKeyAlgorithm.getAlgorithm());
    }
}
