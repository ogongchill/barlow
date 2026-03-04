package com.barlow.infra.storage;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class TestBatchDataSourceConfig {

	@Primary
	@Bean("batchCoreDataSource")
	public DataSource batchCoreDataSource(@Qualifier("coreDataSource") DataSource coreDataSource) {
		return coreDataSource;
	}
}
