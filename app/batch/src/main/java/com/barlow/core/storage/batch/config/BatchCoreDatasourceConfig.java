package com.barlow.core.storage.batch.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class BatchCoreDatasourceConfig {

	@Bean("batchCoreDataSourceProperties")
	@ConfigurationProperties(prefix = "storage.datasource.batch-core")
	public DataSourceProperties dataSourceProperties() {
		return new DataSourceProperties();
	}

	@Bean("batchCoreDataSource")
	public DataSource dataSource(
		@Qualifier("batchCoreDataSourceProperties") DataSourceProperties dataSourceProperties
	) {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(dataSourceProperties.getDriverClassName());
		dataSource.setUrl(dataSourceProperties.getUrl());
		dataSource.setUsername(dataSourceProperties.getUsername());
		dataSource.setPassword(dataSourceProperties.getPassword());
		return dataSource;
	}
}
