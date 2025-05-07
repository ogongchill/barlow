package com.barlow.app.batch.recentbill;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class RecentBillBatchConfig {

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	private final ClassPathResource lawmakerJson;

	public RecentBillBatchConfig(@Value("${file.lawmaker-json}") ClassPathResource lawmakerJson) {
		this.lawmakerJson = lawmakerJson;
	}

	@Bean
	public LawmakerProvider lawmakerProvider() throws IOException {
		return OBJECT_MAPPER.readValue(
			lawmakerJson.getInputStream(),
			OBJECT_MAPPER.getTypeFactory().constructType(LawmakerProvider.class)
		);
	}
}
