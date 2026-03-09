package com.barlow.app.batch.notificationcenter.job.config;

import static com.barlow.app.batch.notificationcenter.NotificationCenterCleanupConstant.CLEANUP_STEP;
import static com.barlow.app.batch.notificationcenter.NotificationCenterCleanupConstant.JOB_NAME;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.barlow.app.batch.common.StepLoggingListener;

@Configuration
public class NotificationCenterCleanupBatchJobConfig {

	private final JobRepository jobRepository;

	public NotificationCenterCleanupBatchJobConfig(
		@Qualifier("batchCoreJobRepository") JobRepository jobRepository) {
		this.jobRepository = jobRepository;
	}

	@Bean
	public Job notificationCenterCleanupJob() {
		return new JobBuilder(JOB_NAME, jobRepository)
			.start(notificationCenterCleanupStep(null, null, null))
			.build();
	}

	@Bean
	@JobScope
	public Step notificationCenterCleanupStep(
		@Qualifier("notificationCenterCleanupTasklet") Tasklet tasklet,
		@Qualifier("batchCoreTransactionManager") PlatformTransactionManager transactionManager,
		StepLoggingListener stepLoggingListener) {
		return new StepBuilder(CLEANUP_STEP, jobRepository)
			.tasklet(tasklet, transactionManager)
			.listener(stepLoggingListener)
			.build();
	}
}
