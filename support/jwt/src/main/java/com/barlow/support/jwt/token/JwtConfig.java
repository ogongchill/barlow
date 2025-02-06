package com.barlow.support.jwt.token;

import com.barlow.support.jwt.crypto.PrivateKeyAlgorithm;
import com.barlow.support.jwt.crypto.PublicKeyAlgorithm;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.spec.InvalidKeySpecException;

@Configuration
public class JwtConfig {

    @Bean
    PublicKeyAlgorithm publicKeyAlgorithm() {
        try {
            return PublicKeyAlgorithm.from("");
        } catch (InvalidKeySpecException e) {
            throw new BeanCreationException("fail during create publicKey", e);
        }
    }

    @Bean
    PrivateKeyAlgorithm privateKeyAlgorithm() {
        try {
            return PrivateKeyAlgorithm.from("");
        } catch (InvalidKeySpecException e) {
            throw new BeanCreationException("fail during create privateKey", e);
        }
    }

    @Bean
    Issuer issuer() {
        return new Issuer("issuer");
    }

    record Issuer(String name) {}
}
