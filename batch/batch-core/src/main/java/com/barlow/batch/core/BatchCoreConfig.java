package com.barlow.batch.core;

import javax.sql.DataSource;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@ComponentScan(basePackages = {
	"com.barlow.core",
	"com.barlow.client.knal",
	"com.barlow.storage.db.core",
	"com.barlow.notification",
	"com.barlow.support.alert"})
@EnableBatchProcessing(
	dataSourceRef = "coreDataSource",
	transactionManagerRef = "coreTransactionManager")
public class BatchCoreConfig {

	@Bean("batchCoreJobRepository")
	public JobRepository jobRepository(
		@Qualifier("coreDataSource") DataSource dataSource,
		@Qualifier("coreTransactionManager") PlatformTransactionManager transactionManager
	) throws Exception {
		JobRepositoryFactoryBean factoryBean = new JobRepositoryFactoryBean();
		factoryBean.setDataSource(dataSource);
		factoryBean.setTransactionManager(transactionManager);
		factoryBean.afterPropertiesSet();
		return factoryBean.getObject();
	}
}
