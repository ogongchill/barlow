package com.barlow.batch.core.tracebill.job.config;

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

@Configuration
public class TraceBillBatchJobConfig {

	private final JobRepository jobRepository;

	public TraceBillBatchJobConfig(@Qualifier("batchCoreJobRepository") JobRepository jobRepository) {
		this.jobRepository = jobRepository;
	}

	@Bean
	public Job traceBillJob() {
		return new JobBuilder("traceBillJob", jobRepository)
			.start(traceBillDirtyCheckStep(null, null))
			.next(traceBillUpdateStep(null, null))
			.next(traceBillNotifyStep(null, null))
			.build();
	}

	@Bean
	@JobScope
	public Step traceBillDirtyCheckStep(
		@Qualifier("traceBillDirtyCheckTasklet") Tasklet tasklet,
		@Qualifier("coreTransactionManager") PlatformTransactionManager transactionManager
	) {
		return new StepBuilder("traceBillDirtyCheckStep", jobRepository)
			.tasklet(tasklet, transactionManager)
			.build();
	}

	@Bean
	@JobScope
	public Step traceBillUpdateStep(
		@Qualifier("traceBillUpdateTasklet") Tasklet tasklet,
		@Qualifier("coreTransactionManager") PlatformTransactionManager transactionManager
	) {
		return new StepBuilder("traceBillUpdateStep", jobRepository)
			.tasklet(tasklet, transactionManager)
			.build();
	}

	@Bean
	@JobScope
	public Step traceBillNotifyStep(
		@Qualifier("traceBillNotifyTasklet") Tasklet tasklet,
		@Qualifier("coreTransactionManager") PlatformTransactionManager transactionManager
	) {
		return new StepBuilder("traceBillNotifyStep", jobRepository)
			.tasklet(tasklet, transactionManager)
			.build();
	}
}
