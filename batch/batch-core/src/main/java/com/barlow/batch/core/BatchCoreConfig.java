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
	"com.barlow.core.enumerate",
	"com.barlow.client.knal",
	"com.barlow.storage.db.core.batch",
	"com.barlow.storage.db.core.notification",
	"com.barlow.notification",
	"com.barlow.support.alert"})
@EnableBatchProcessing(
	dataSourceRef = "batchCoreDataSource",
	transactionManagerRef = "batchCoreTransactionManager")
public class BatchCoreConfig {

	@Bean("batchCoreJobRepository")
	public JobRepository jobRepository(
		@Qualifier("batchCoreDataSource") DataSource dataSource,
		@Qualifier("batchCoreTransactionManager") PlatformTransactionManager transactionManager
	) throws Exception {
		JobRepositoryFactoryBean factoryBean = new JobRepositoryFactoryBean();
		factoryBean.setDataSource(dataSource);
		factoryBean.setTransactionManager(transactionManager);
		factoryBean.afterPropertiesSet();
		return factoryBean.getObject();
	}
}
