package com.barlow.batch.core.tracebill.job.config;

import static com.barlow.batch.core.tracebill.TraceBillConstant.FIRST_STEP_NAME;
import static com.barlow.batch.core.tracebill.TraceBillConstant.JOB_NAME;
import static com.barlow.batch.core.tracebill.TraceBillConstant.SECOND_STEP_NAME;
import static com.barlow.batch.core.tracebill.TraceBillConstant.THIRD_STEP_NAME;

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

import com.barlow.batch.core.common.StepLoggingListener;

@Configuration
public class TraceBillBatchJobConfig {

	private final JobRepository jobRepository;

	public TraceBillBatchJobConfig(@Qualifier("batchCoreJobRepository") JobRepository jobRepository) {
		this.jobRepository = jobRepository;
	}

	@Bean
	public Job traceBillJob() {
		return new JobBuilder(JOB_NAME, jobRepository)
			.start(traceBillDirtyCheckStep(null, null, null))
			.next(traceBillUpdateStep(null, null, null))
			.next(traceBillNotifyStep(null, null, null))
			.build();
	}

	@Bean
	@JobScope
	public Step traceBillDirtyCheckStep(
		@Qualifier("traceBillDirtyCheckTasklet") Tasklet tasklet,
		@Qualifier("coreTransactionManager") PlatformTransactionManager transactionManager,
		StepLoggingListener stepLoggingListener
	) {
		return new StepBuilder(FIRST_STEP_NAME, jobRepository)
			.tasklet(tasklet, transactionManager)
			.listener(stepLoggingListener)
			.build();
	}

	@Bean
	@JobScope
	public Step traceBillUpdateStep(
		@Qualifier("traceBillUpdateTasklet") Tasklet tasklet,
		@Qualifier("coreTransactionManager") PlatformTransactionManager transactionManager,
		StepLoggingListener stepLoggingListener
	) {
		return new StepBuilder(SECOND_STEP_NAME, jobRepository)
			.tasklet(tasklet, transactionManager)
			.listener(stepLoggingListener)
			.build();
	}

	@Bean
	@JobScope
	public Step traceBillNotifyStep(
		@Qualifier("traceBillNotifyTasklet") Tasklet tasklet,
		@Qualifier("coreTransactionManager") PlatformTransactionManager transactionManager,
		StepLoggingListener stepLoggingListener
	) {
		return new StepBuilder(THIRD_STEP_NAME, jobRepository)
			.tasklet(tasklet, transactionManager)
			.listener(stepLoggingListener)
			.build();
	}
}
