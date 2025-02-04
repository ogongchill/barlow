package com.barlow.batch.core.recentbill.job.config;

import org.springframework.batch.core.Job;
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

import com.barlow.batch.core.recentbill.job.step.BillProposer;

@Configuration
public class TodayBillCreateBatchJobConfig {

	private final JobRepository jobRepository;

	public TodayBillCreateBatchJobConfig(@Qualifier("batchCoreJobRepository") JobRepository jobRepository) {
		this.jobRepository = jobRepository;
	}

	@Bean
	public Job todayBillCreateBatchJob() {
		return new JobBuilder("todayBillCreateBatchJob", jobRepository)
			.start(writeTodayBillInfoStep(null, null))
			.next(setBillProposerStep(null, null, null, null))
			.build();
	}

	@Bean
	@JobScope
	public Step writeTodayBillInfoStep(
		@Qualifier("todayBillInfoWriteTasklet") Tasklet tasklet,
		@Qualifier("coreTransactionManager") PlatformTransactionManager transactionManager
	) {
		return new StepBuilder("writeTodayBillInfoStep", jobRepository)
			.tasklet(tasklet, transactionManager)
			.build();
	}

	@Bean
	@JobScope
	public Step setBillProposerStep(
		@Value("#{jobParameters[chunkSize]}") Integer chunkSize,
		@Qualifier("coreTransactionManager") PlatformTransactionManager transactionManager,
		ItemReader<BillProposer> billProposerReader,
		ItemWriter<BillProposer> billProposerWriter
	) {
		return new StepBuilder("setBillProposerStep", jobRepository)
			.<BillProposer, BillProposer>chunk(chunkSize, transactionManager)
			.reader(billProposerReader)
			.writer(billProposerWriter)
			.build();
	}

	@Bean
	@JobScope
	public Step notifyTodayBillStep(
		@Qualifier("todayBillNotifierTasklet") Tasklet tasklet,
		@Qualifier("coreTransactionManager") PlatformTransactionManager transactionManager
	) {
		DefaultTransactionAttribute transactionAttribute = new DefaultTransactionAttribute();
		transactionAttribute.setPropagationBehavior(TransactionDefinition.PROPAGATION_NEVER);
		return new StepBuilder("todayBillNotifierTasklet", jobRepository)
			.tasklet(tasklet, transactionManager)
			.transactionAttribute(transactionAttribute)
			.build();
	}
}
