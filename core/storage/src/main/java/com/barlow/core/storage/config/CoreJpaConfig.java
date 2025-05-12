package com.barlow.core.storage.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import jakarta.persistence.EntityManagerFactory;

@Configuration
@EnableTransactionManagement
@EntityScan(basePackages = "com.barlow.core.storage")
@EnableJpaRepositories(
	basePackages = "com.barlow.core.storage",
	entityManagerFactoryRef = "coreEntityManagerFactory",
	transactionManagerRef = "coreTransactionManager")
public class CoreJpaConfig {

	@Bean("coreTransactionManager")
	public PlatformTransactionManager platformTransactionManager(
		@Qualifier("coreEntityManagerFactory") EntityManagerFactory emf
	) {
		return new JpaTransactionManager(emf);
	}

	@Bean("coreEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(
		@Qualifier("coreDataSource") DataSource dataSource,
		CoreJpaProperties jpaProperties,
		EntityManagerFactoryBuilder builder
	) {
		return builder.dataSource(dataSource)
			.packages("com.barlow.core.storage")
			.persistenceUnit("core")
			.properties(jpaProperties.properties)
			.build();
	}

	@Component
	@ConfigurationProperties(prefix = "spring.jpa")
	public static class CoreJpaProperties {
		private final Map<String, Object> properties = new HashMap<>();
	}
}
