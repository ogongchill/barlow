package com.barlow.batch.core;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class BatchCoreDataSourceConfig {

	@Bean
	@ConfigurationProperties(prefix = "spring.datasource.batch")
	public HikariConfig batchHikariConfig() {
		return new HikariConfig();
	}

	@Primary
	@Bean("dataSource")
	public DataSource batchDataSource(@Qualifier("batchHikariConfig") HikariConfig config) {
		return new HikariDataSource(config);
	}

	@Primary
	@Bean("transactionManager")
	public PlatformTransactionManager batchTransactionManager(@Qualifier("dataSource") DataSource dataSource) {
		return new JdbcTransactionManager(dataSource);
	}
}
