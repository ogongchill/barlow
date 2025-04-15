package com.barlow.batch.core.tracebill.job.config;

import static com.barlow.batch.core.tracebill.TraceBillConstant.TRACE_BILL_DIRTY_CHECK_STEP;
import static com.barlow.batch.core.tracebill.TraceBillConstant.JOB_NAME;
import static com.barlow.batch.core.tracebill.TraceBillConstant.TRACE_BILL_UPDATE_STEP;
import static com.barlow.batch.core.tracebill.TraceBillConstant.TRACE_BILL_NOTIFY_STEP;

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
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;

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
		@Qualifier("batchCoreTransactionManager") PlatformTransactionManager transactionManager,
		StepLoggingListener stepLoggingListener
	) {
		return new StepBuilder(TRACE_BILL_DIRTY_CHECK_STEP, jobRepository)
			.tasklet(tasklet, transactionManager)
			.listener(stepLoggingListener)
			.build();
	}

	@Bean
	@JobScope
	public Step traceBillUpdateStep(
		@Qualifier("traceBillUpdateTasklet") Tasklet tasklet,
		@Qualifier("batchCoreTransactionManager") PlatformTransactionManager transactionManager,
		StepLoggingListener stepLoggingListener
	) {
		return new StepBuilder(TRACE_BILL_UPDATE_STEP, jobRepository)
			.tasklet(tasklet, transactionManager)
			.listener(stepLoggingListener)
			.build();
	}

	@Bean
	@JobScope
	public Step traceBillNotifyStep(
		@Qualifier("traceBillNotifyTasklet") Tasklet tasklet,
		@Qualifier("batchCoreTransactionManager") PlatformTransactionManager transactionManager,
		StepLoggingListener stepLoggingListener
	) {
		DefaultTransactionAttribute transactionAttribute = new DefaultTransactionAttribute();
		transactionAttribute.setPropagationBehavior(TransactionDefinition.PROPAGATION_NEVER);
		return new StepBuilder(TRACE_BILL_NOTIFY_STEP, jobRepository)
			.tasklet(tasklet, transactionManager)
			.transactionAttribute(transactionAttribute)
			.listener(stepLoggingListener)
			.build();
	}
}
