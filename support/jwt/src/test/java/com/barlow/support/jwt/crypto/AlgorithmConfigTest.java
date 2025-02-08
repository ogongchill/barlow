package com.barlow.support.jwt.crypto;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;


import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest(classes = AlgorithmConfig.class)
class AlgorithmConfigTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withUserConfiguration(AlgorithmConfig.class);

    @DisplayName("privatekey와 publickey가 bean으로 등록되는지 확인")
    @Test
    void beanCreationTest() {
        assertDoesNotThrow(() -> {
            contextRunner.run(context -> context.getBean(PublicKeyAlgorithm.class))
                    .run(context -> context.getBean(PrivateKeyAlgorithm.class));
        });
    }

    @DisplayName("privateKey와 publicKey 쌍이 유효한지 확인")
    @Test
    void testValidKeyPair() {
        contextRunner.run(context -> {
            Algorithm publicKeyAlgorithm = context.getBean(PublicKeyAlgorithm.class).getAlgorithm();
            Algorithm privateKeyAlgorithm = context.getBean(PrivateKeyAlgorithm.class).getAlgorithm();
            assertDoesNotThrow(()->{
                String token = JWT.create().sign(privateKeyAlgorithm);
                JWT.require(publicKeyAlgorithm).build().verify(token);
            });
        });
    }
}