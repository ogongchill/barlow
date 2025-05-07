package com.barlow.services.auth.config;

import com.auth0.jwt.algorithms.Algorithm;

import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.security.spec.InvalidKeySpecException;

@TestConfiguration
public class TestKeyConfig {

	private static final RSAKeyFactory RSA_KEY_FACTORY = new RSAKeyFactory();

	@Bean("testPrivateKeyAlgorithm")
	public Algorithm privateKeyAlgorithm(@Value("${auth.jwt.crypto.private-key}") String privateKeyStr) {
		try {
			return new AlgorithmConfig().jwtPrivateKeyAlgorithm(privateKeyStr, RSA_KEY_FACTORY);
		} catch (InvalidKeySpecException e) {
			throw new BeanCreationException("private key generation failed", e);
		}
	}

	@Bean("testPublicKeyAlgorithm")
	public Algorithm publicKeyAlgorithm(@Value("${auth.jwt.crypto.public-key}") String publicKeyStr) {
		try {
			return new AlgorithmConfig().jwtPublicKeyAlgorithm(publicKeyStr, RSA_KEY_FACTORY);
		} catch (InvalidKeySpecException e) {
			throw new BeanCreationException("public key generation failed", e);
		}
	}
}
