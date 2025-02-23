package com.barlow.batch.core.recentbill.job.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;

import com.barlow.batch.core.recentbill.job.listener.BillProposerReaderStepExecutionContextSharingListener;
import com.barlow.batch.core.recentbill.job.listener.StepLoggingListener;
import com.barlow.batch.core.recentbill.job.step.BillProposer;

@Configuration
public class TodayBillCreateBatchJobConfig {

	private final JobRepository jobRepository;

	public TodayBillCreateBatchJobConfig(@Qualifier("batchCoreJobRepository") JobRepository jobRepository) {
		this.jobRepository = jobRepository;
	}

	@Bean
	public Job todayBillCreateBatchJob(JobExecutionListener jobExecutionListener) {
		return new JobBuilder("todayBillCreateBatchJob", jobRepository)
			.listener(jobExecutionListener)
			.start(writeTodayBillInfoStep(null, null, null))
			.next(writeBillProposerStep(null, null, null, null, null, null))
			.next(notifyTodayBillStep(null, null, null))
			.build();
	}

	@Bean
	@JobScope
	public Step writeTodayBillInfoStep(
		@Qualifier("todayBillInfoWriteTasklet") Tasklet tasklet,
		@Qualifier("coreTransactionManager") PlatformTransactionManager transactionManager,
		StepLoggingListener stepLoggingListener
	) {
		return new StepBuilder("writeTodayBillInfoStep", jobRepository)
			.tasklet(tasklet, transactionManager)
			.listener(stepLoggingListener)
			.build();
	}

	@Bean
	@JobScope
	public Step writeBillProposerStep(
		@Value("#{jobParameters[chunkSize]}") Integer chunkSize,
		@Qualifier("coreTransactionManager") PlatformTransactionManager transactionManager,
		StepLoggingListener stepLoggingListener,
		BillProposerReaderStepExecutionContextSharingListener stepExecutionContextSharingListener,
		ItemReader<BillProposer> billProposerReader,
		ItemWriter<BillProposer> billProposerWriter
	) {
		return new StepBuilder("writeBillProposerStep", jobRepository)
			.<BillProposer, BillProposer>chunk(chunkSize, transactionManager)
			.reader(billProposerReader)
			.writer(billProposerWriter)
			.listener(stepExecutionContextSharingListener)
			.listener(stepLoggingListener)
			.build();
	}

	@Bean
	@JobScope
	public Step notifyTodayBillStep(
		@Qualifier("todayBillNotifierTasklet") Tasklet tasklet,
		@Qualifier("coreTransactionManager") PlatformTransactionManager transactionManager,
		StepLoggingListener stepLoggingListener
	) {
		DefaultTransactionAttribute transactionAttribute = new DefaultTransactionAttribute();
		transactionAttribute.setPropagationBehavior(TransactionDefinition.PROPAGATION_NEVER);
		return new StepBuilder("todayBillNotifierTasklet", jobRepository)
			.tasklet(tasklet, transactionManager)
			.transactionAttribute(transactionAttribute)
			.listener(stepLoggingListener)
			.build();
	}
}
