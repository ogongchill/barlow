package com.barlow.batch.core.preannounce.job.config;

import static com.barlow.batch.core.preannounce.PreAnnounceConstant.JOB_NAME;
import static com.barlow.batch.core.preannounce.PreAnnounceConstant.UPDATE_STEP;
import static com.barlow.batch.core.preannounce.PreAnnounceConstant.WRITE_STEP;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
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

import com.barlow.batch.core.common.StepLoggingListener;

@Configuration
public class PreAnnounceBillBatchJobConfig {

	private final JobRepository jobRepository;

	public PreAnnounceBillBatchJobConfig(@Qualifier("batchCoreJobRepository") JobRepository jobRepository) {
		this.jobRepository = jobRepository;
	}

	@Bean
	public Job preAnnounceBillBatchJob(
		@Qualifier("preAnnounceBillDirtyCheckJobListener") JobExecutionListener jobExecutionListener
	) {
		return new JobBuilder(JOB_NAME, jobRepository)
			.listener(jobExecutionListener)
			.start(billPostPreAnnounceInfoUpdateStep(null, null, null))
			.next(preAnnounceBillWriteStep(null, null, null))
			.build();
	}

	@Bean
	@JobScope
	public Step billPostPreAnnounceInfoUpdateStep(
		@Qualifier("billPostPreAnnounceInfoUpdateTasklet") Tasklet tasklet,
		@Qualifier("coreTransactionManager") PlatformTransactionManager transactionManager,
		StepLoggingListener stepLoggingListener
	) {
		return new StepBuilder(UPDATE_STEP, jobRepository)
			.tasklet(tasklet, transactionManager)
			.listener(stepLoggingListener)
			.build();
	}

	@Bean
	@JobScope
	public Step preAnnounceBillWriteStep(
		@Qualifier("preAnnounceBillWriteTasklet") Tasklet tasklet,
		@Qualifier("coreTransactionManager") PlatformTransactionManager transactionManager,
		StepLoggingListener stepLoggingListener
	) {
		return new StepBuilder(WRITE_STEP, jobRepository)
			.tasklet(tasklet, transactionManager)
			.listener(stepLoggingListener)
			.build();
	}
}
