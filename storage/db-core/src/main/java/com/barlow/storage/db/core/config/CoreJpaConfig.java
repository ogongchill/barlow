package com.barlow.storage.db.core.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import jakarta.persistence.EntityManagerFactory;

@Configuration
@EnableTransactionManagement
@EntityScan(basePackages = "com.barlow.storage.db.core")
@EnableJpaRepositories(basePackages = "com.barlow.storage.db.core")
public class CoreJpaConfig {

	@Bean("coreTransactionManager")
	public PlatformTransactionManager platformTransactionManager(
		EntityManagerFactory emf,
		@Qualifier("coreDataSource") DataSource dataSource
	) {
		JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
		jpaTransactionManager.setEntityManagerFactory(emf);
		jpaTransactionManager.setDataSource(dataSource);
		return jpaTransactionManager;
	}
}
