package com.barlow.storage.db.core.batch.config;

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

import com.barlow.storage.db.core.BillPostJpaEntity;
import com.barlow.storage.db.core.LegislationAccountJpaEntity;
import com.barlow.storage.db.core.NotificationConfigJpaEntity;

import jakarta.persistence.EntityManagerFactory;

@Configuration
@EnableTransactionManagement
@EntityScan(basePackageClasses = {
	BillPostJpaEntity.class,
	LegislationAccountJpaEntity.class,
	NotificationConfigJpaEntity.class})
@EnableJpaRepositories(
	basePackages = {"com.barlow.storage.db.core.batch", "com.barlow.storage.db.core.notification"},
	entityManagerFactoryRef = "batchCoreEntityManagerFactory",
	transactionManagerRef = "batchCoreTransactionManager")
public class BatchCoreJpaConfig {

	@Bean("batchCoreTransactionManager")
	public PlatformTransactionManager platformTransactionManager(
		@Qualifier("batchCoreEntityManagerFactory") EntityManagerFactory emf
	) {
		return new JpaTransactionManager(emf);
	}

	@Bean("batchCoreEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(
		@Qualifier("batchCoreDataSource") DataSource dataSource,
		BatchCoreJpaProperties jpaProperties,
		EntityManagerFactoryBuilder builder
	) {
		return builder.dataSource(dataSource)
			.packages("com.barlow.storage.db.core.batch", "com.barlow.storage.db.core.notification")
			.packages(BillPostJpaEntity.class, LegislationAccountJpaEntity.class, NotificationConfigJpaEntity.class)
			.persistenceUnit("batch-core")
			.properties(jpaProperties.properties)
			.build();
	}

	@Component
	@ConfigurationProperties(prefix = "spring.jpa")
	public static class BatchCoreJpaProperties {
		private final Map<String, Object> properties = new HashMap<>();
	}
}
