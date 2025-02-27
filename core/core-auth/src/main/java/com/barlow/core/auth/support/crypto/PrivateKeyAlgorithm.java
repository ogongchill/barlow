package com.barlow.core.auth.support.crypto;

import com.auth0.jwt.algorithms.Algorithm;

import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;

public class PrivateKeyAlgorithm extends KeyAlgorithm {

    protected PrivateKeyAlgorithm(Algorithm algorithm) {
        super(algorithm);
    }

    public static PrivateKeyAlgorithm from(String keyString) throws InvalidKeySpecException {
        RSAPrivateKey privateKey = RSAKeyFactory.INSTANCE.createPrivateKey(keyString);
        return new PrivateKeyAlgorithm(Algorithm.RSA256(privateKey));
    }
}
