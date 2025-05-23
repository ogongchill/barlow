package com.barlow.services.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TokenConfig {

	@Bean
	public JwtConfig accessTokenConfig() {
		return JwtConfig.createAccessToken();
	}

	@Bean
	public JwtConfig refreshTokenConfig() {
		return JwtConfig.createRefreshToken();
	}
}
