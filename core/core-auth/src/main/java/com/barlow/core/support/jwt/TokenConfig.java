package com.barlow.core.support.jwt;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class TokenConfig {

    @Bean
    public JwtConfig accessTokenConfig() {
        return new JwtConfig("barlow", Duration.ofDays(7));
    }
}
