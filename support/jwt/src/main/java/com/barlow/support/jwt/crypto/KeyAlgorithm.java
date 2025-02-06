package com.barlow.support.jwt.crypto;

import com.auth0.jwt.algorithms.Algorithm;

public abstract class KeyAlgorithm {

    private final Algorithm algorithm;

    protected KeyAlgorithm(Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    public Algorithm getAlgorithm() {
        return algorithm;
    }
}
