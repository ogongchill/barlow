package com.barlow.storage.db.core.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import jakarta.persistence.EntityManagerFactory;

@Configuration
@EnableTransactionManagement
@EntityScan(basePackages = "com.barlow.storage.db.core")
@EnableJpaRepositories(
	basePackages = "com.barlow.storage.db.core",
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
		EntityManagerFactoryBuilder builder
	) {
		return builder.dataSource(dataSource)
			.packages("com.barlow.storage.db.core")
			.persistenceUnit("core")
			.build();
	}
}
