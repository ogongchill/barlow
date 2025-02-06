package com.barlow.core.auth.authorization;

import com.barlow.core.auth.crypto.PrivateKeyAlgorithm;
import com.barlow.core.auth.jwt.Payload;
import com.barlow.core.auth.utils.TestKeys;
import org.junit.jupiter.api.Test;

import java.security.spec.InvalidKeySpecException;

class TokenSignerTest {

    @Test
    void create() throws InvalidKeySpecException {
        TokenSigner signer = new TokenSigner(PrivateKeyAlgorithm.from(TestKeys.PRIVATE.getKeyString()));
        Payload payload = Payload.withIssuer("issuer")
                .withClaim("memberNo", "1")
                .create();
        String jwt = signer.sign(payload);
    }
}