package com.barlow.support.jwt.crypto;

import com.auth0.jwt.algorithms.Algorithm;

import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;

public class PublicKeyAlgorithm extends KeyAlgorithm {

    protected PublicKeyAlgorithm(Algorithm algorithm) {
        super(algorithm);
    }

    public static PublicKeyAlgorithm from(String keyString) throws InvalidKeySpecException {
        RSAPublicKey publicKey = RSAKeyFactory.INSTANCE.createPublicKey(keyString);
        return new PublicKeyAlgorithm(Algorithm.RSA256(publicKey));
    }
}
