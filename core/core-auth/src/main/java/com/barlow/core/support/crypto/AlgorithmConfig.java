package com.barlow.core.support.crypto;

import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.spec.InvalidKeySpecException;

@Configuration
@PropertySource(value = "classpath:crypto.properties")
public class AlgorithmConfig {

    @Value("${crypto.private}")
    private String privateKeyPath;

    @Value("${crypto.public}")
    private String publicKeyPath;

    @Bean
    PublicKeyAlgorithm publicKeyAlgorithm() {
        try {
            String keyString = FileReader.read(publicKeyPath);
            return PublicKeyAlgorithm.from(keyString);
        } catch (InvalidKeySpecException | IOException e) {
            throw new BeanCreationException("fail during create publicKey", e);
        }
    }

    @Bean
    PrivateKeyAlgorithm privateKeyAlgorithm() {
        try {
            String keyString = FileReader.read(privateKeyPath);
            return PrivateKeyAlgorithm.from(keyString);
        } catch (InvalidKeySpecException | IOException e) {
            throw new BeanCreationException("fail during create privateKey", e);
        }
    }


    static class FileReader {
        public static String read(String fileName) throws IOException {
            ClassPathResource resource = new ClassPathResource(fileName);
            InputStream inputStream = resource.getInputStream();
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
