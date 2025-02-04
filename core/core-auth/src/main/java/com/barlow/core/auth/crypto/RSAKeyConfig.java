package com.barlow.core.auth.crypto;

import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.spec.InvalidKeySpecException;

@Configuration
public class RSAKeyConfig {

    @Bean
    PublicKeyAlgorithm publicKeyAlgorithm() {
        try {
            return PublicKeyAlgorithm.from("");
        } catch (InvalidKeySpecException e) {
            throw new BeanCreationException("fail creating public key" ,e);
        }
    }

    @Bean
    PrivateKeyAlgorithm privateKeyAlgorithm() {
        try {
            return PrivateKeyAlgorithm.from("");
        } catch (InvalidKeySpecException e) {
            throw new BeanCreationException("fail creating private key", e);
        }
    }
}
