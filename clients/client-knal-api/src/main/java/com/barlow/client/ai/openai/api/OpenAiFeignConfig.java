package com.barlow.client.ai.openai.api;

import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAiFeignConfig {

    @Value("${ai.open-ai.api.service-key}")
    private String apiKey;

    @Bean
    public RequestInterceptor openAiRequestInterceptor() {
        return template -> {
            template.header("Authorization", "Bearer " + apiKey);
            template.header("Content-Type", "application/json");
        };
    }

    @Bean
    public ErrorDecoder customErrorDecoder() {
        return new OpenAiErrorDecoder();
    }
}
