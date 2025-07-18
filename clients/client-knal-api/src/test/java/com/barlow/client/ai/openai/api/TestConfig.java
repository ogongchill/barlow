package com.barlow.client.ai.openai.api;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(clients = TestClient.class)
@EnableAutoConfiguration
public class TestConfig {
}
