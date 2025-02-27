package com.barlow.core.auth.support.crypto;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.barlow.core.support.crypto.AlgorithmConfig;

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
			contextRunner.run(context -> context.getBean("testPrivateKeyAlgorithm", Algorithm.class))
				.run(context -> context.getBean("testPublicKeyAlgorithm", Algorithm.class));
		});
	}

	@DisplayName("privateKey와 publicKey 쌍이 유효한지 확인")
	@Test
	void testValidKeyPair() {
		contextRunner.run(context -> {
			Algorithm privateKeyAlgorithm = context.getBean("testPrivateKeyAlgorithm", Algorithm.class);
			Algorithm publicKeyAlgorithm = context.getBean("testPublicKeyAlgorithm", Algorithm.class);
			assertDoesNotThrow(() -> {
				String token = JWT.create().sign(privateKeyAlgorithm);
				JWT.require(publicKeyAlgorithm).build().verify(token);
			});
		});
	}
}