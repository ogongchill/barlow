package com.barlow.app.batch.notificationcenter.job.config;

import static com.barlow.app.batch.notificationcenter.NotificationCenterConstant.JOB_NAME;
import static com.barlow.app.batch.notificationcenter.NotificationCenterConstant.STEP_NAME;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.barlow.app.batch.common.StepLoggingListener;
import com.barlow.app.batch.notificationcenter.job.step.NotificationCenterCleanupTasklet;

@Configuration
public class NotificationCenterCleanupBatchJobConfig {

	private final JobRepository jobRepository;
	private final StepLoggingListener stepLoggingListener;

	public NotificationCenterCleanupBatchJobConfig(
		@Qualifier("batchCoreJobRepository") JobRepository jobRepository,
		StepLoggingListener stepLoggingListener) {
		this.jobRepository = jobRepository;
		this.stepLoggingListener = stepLoggingListener;
	}

	@Bean(JOB_NAME)
	public Job notificationCenterCleanupBatchJob(Step notificationCenterCleanupStep) {
		return new JobBuilder(JOB_NAME, jobRepository).start(notificationCenterCleanupStep).build();
	}

	@Bean
	@JobScope
	public Step notificationCenterCleanupStep(NotificationCenterCleanupTasklet tasklet,
		@Qualifier("batchCoreTransactionManager") PlatformTransactionManager transactionManager) {
		return new StepBuilder(STEP_NAME, jobRepository).tasklet(tasklet, transactionManager)
			.listener(stepLoggingListener).build();
	}
}
