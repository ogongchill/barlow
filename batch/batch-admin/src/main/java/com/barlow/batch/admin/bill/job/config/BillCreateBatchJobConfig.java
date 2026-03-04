package com.barlow.batch.admin.bill.job.config;

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

import com.barlow.batch.admin.bill.job.listener.AdminWriteAllBillProposerStepListener;
import com.barlow.batch.admin.bill.job.listener.WriteBillLegislationBodyStepListener;
import com.barlow.batch.admin.bill.job.step.BillLegislationBody;
import com.barlow.batch.admin.bill.job.step.BillProposer;
import com.barlow.batch.admin.common.AdminStepLoggingListener;
import com.barlow.infra.knal.opendata.api.OpenDataException;

@Configuration
public class BillCreateBatchJobConfig {

	private final JobRepository jobRepository;

	public BillCreateBatchJobConfig(@Qualifier("batchCoreJobRepository") JobRepository jobRepository) {
		this.jobRepository = jobRepository;
	}

	@Bean
	public Job billCreateBatchJob(
		@Qualifier("retrieveBillJobListener") JobExecutionListener jobExecutionListener
	) {
		return new JobBuilder("billCreateBatchJob", jobRepository)
			.listener(jobExecutionListener)
			.start(writeBillInfoStep(null, null, null))
			.next(writeAllBillProposerStep(null, null, null, null, null, null))
			.next(writeBillLegislationBodyStep(null, null, null, null, null, null))
			.build();
	}

	@Bean
	@JobScope
	public Step writeBillInfoStep(
		@Qualifier("billInfoWriteTasklet") Tasklet tasklet,
		@Qualifier("batchCoreTransactionManager") PlatformTransactionManager transactionManager,
		AdminStepLoggingListener adminStepLoggingListener
	) {
		return new StepBuilder("writeBillInfoStep", jobRepository)
			.tasklet(tasklet, transactionManager)
			.listener(adminStepLoggingListener)
			.build();
	}

	@Bean
	@JobScope
	public Step writeAllBillProposerStep(
		@Value("#{jobParameters[chunkSize]}") Integer chunkSize,
		@Qualifier("batchCoreTransactionManager") PlatformTransactionManager transactionManager,
		AdminStepLoggingListener adminStepLoggingListener,
		AdminWriteAllBillProposerStepListener stepExecutionContextSharingListener,
		ItemReader<BillProposer> billProposerReader,
		ItemWriter<BillProposer> billProposerWriter
	) {
		return new StepBuilder("writeAllBillProposerStep", jobRepository)
			.<BillProposer, BillProposer>chunk(chunkSize, transactionManager)
			.reader(billProposerReader)
			.writer(billProposerWriter)
			.listener(stepExecutionContextSharingListener)
			.listener(adminStepLoggingListener)
			.faultTolerant()
			.skip(OpenDataException.class)
			.build();
	}

	@Bean
	@JobScope
	public Step writeBillLegislationBodyStep(
		@Value("#{jobParameters[chunkSize]}") Integer chunkSize,
		@Qualifier("batchCoreTransactionManager") PlatformTransactionManager transactionManager,
		AdminStepLoggingListener adminStepLoggingListener,
		WriteBillLegislationBodyStepListener stepExecutionContextSharingListener,
		ItemReader<BillLegislationBody> billProposerReader,
		ItemWriter<BillLegislationBody> billProposerWriter
	) {
		return new StepBuilder("writeBillLegislationBodyStep", jobRepository)
			.<BillLegislationBody, BillLegislationBody>chunk(chunkSize, transactionManager)
			.reader(billProposerReader)
			.writer(billProposerWriter)
			.listener(stepExecutionContextSharingListener)
			.listener(adminStepLoggingListener)
			.faultTolerant()
			.skip(OpenDataException.class)
			.build();
	}
}
