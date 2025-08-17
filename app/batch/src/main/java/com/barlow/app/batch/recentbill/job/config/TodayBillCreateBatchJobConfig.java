package com.barlow.app.batch.recentbill.job.config;

import com.barlow.app.batch.recentbill.job.TodayBillInfoBatchEntity;
import com.barlow.app.batch.summarization.common.SummaryRequestStatus;
import com.barlow.app.batch.summarization.step.request.BackgroundRequestWriter;
import com.barlow.client.ai.openai.api.exception.OpenAiException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.integration.async.AsyncItemProcessor;
import org.springframework.batch.integration.async.AsyncItemWriter;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.BackOffPolicy;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;

import com.barlow.app.batch.recentbill.job.listener.BillProposerReaderStepExecutionContextSharingListener;
import com.barlow.app.batch.recentbill.job.step.BillProposer;
import com.barlow.app.batch.common.StepLoggingListener;
import com.barlow.client.knal.opendata.api.OpenDataException;

import java.util.concurrent.Future;

import static com.barlow.app.batch.recentbill.RecentBillConstant.*;

@Configuration
public class TodayBillCreateBatchJobConfig {

	private final JobRepository jobRepository;

	public TodayBillCreateBatchJobConfig(@Qualifier("batchCoreJobRepository") JobRepository jobRepository) {
		this.jobRepository = jobRepository;
	}

	@Bean
	public Job todayBillCreateBatchJob(
		@Qualifier("retrieveTodayBillJobListener") JobExecutionListener jobExecutionListener
	) {
		return new JobBuilder(JOB_NAME, jobRepository)
			.listener(jobExecutionListener)
			.start(requestBackgroundSummaryStep( null,null, null, null, null, null, null))
			.next(pollSummaryRequestStep(null,null,null))
			.next(writeTodayBillInfoStep(null, null, null))
			.next(writeBillProposerStep(null, null, null, null, null, null))
			.next(notifyTodayBillStep(null, null, null))
			.build();
	}

	@Bean
	@JobScope
	public Step writeTodayBillInfoStep(
		@Qualifier("todayBillInfoWriteTasklet") Tasklet tasklet,
		@Qualifier("batchCoreTransactionManager") PlatformTransactionManager transactionManager,
		StepLoggingListener stepLoggingListener
	) {
		return new StepBuilder(WRITE_TODAY_BILL_INFO_STEP, jobRepository)
			.tasklet(tasklet, transactionManager)
			.listener(stepLoggingListener)
			.build();
	}

	@Bean
	@JobScope
	public Step writeBillProposerStep(
		@Value("#{jobParameters[chunkSize]}") Integer chunkSize,
		@Qualifier("batchCoreTransactionManager") PlatformTransactionManager transactionManager,
		StepLoggingListener stepLoggingListener,
		BillProposerReaderStepExecutionContextSharingListener stepExecutionContextSharingListener,
		ItemReader<BillProposer> billProposerReader,
		ItemWriter<BillProposer> billProposerWriter
	) {
		return new StepBuilder(WRITE_BILL_PROPOSER_STEP, jobRepository)
			.<BillProposer, BillProposer>chunk(chunkSize, transactionManager)
			.reader(billProposerReader)
			.writer(billProposerWriter)
			.listener(stepExecutionContextSharingListener)
			.listener(stepLoggingListener)
			.faultTolerant()
			.skip(OpenDataException.class)
			.build();
	}

	@Bean
	@JobScope
	public Step notifyTodayBillStep(
		@Qualifier("todayBillNotifyTasklet") Tasklet tasklet,
		@Qualifier("batchCoreTransactionManager") PlatformTransactionManager transactionManager,
		StepLoggingListener stepLoggingListener
	) {
		DefaultTransactionAttribute transactionAttribute = new DefaultTransactionAttribute();
		transactionAttribute.setPropagationBehavior(TransactionDefinition.PROPAGATION_NEVER);
		return new StepBuilder(TODAY_BILL_NOTIFY_STEP, jobRepository)
			.tasklet(tasklet, transactionManager)
			.transactionAttribute(transactionAttribute)
			.listener(stepLoggingListener)
			.build();
	}

	@Bean
	@JobScope
	public Step pollSummaryRequestStep(
			@Qualifier("billAiSummaryPollTasklet") Tasklet tasklet,
			@Qualifier("batchCoreTransactionManager") PlatformTransactionManager platformTransactionManager,
			StepLoggingListener stepLoggingListener
	){
		return new StepBuilder(POLL_SUMMARY_REQUEST_STEP, jobRepository)
				.tasklet(tasklet, platformTransactionManager)
				.listener(stepLoggingListener)
				.build();
	}

	@Bean
	@JobScope
	public Step requestBackgroundSummaryStep(
			@Value("#{jobParameters[chunkSize]}") Integer chunkSize,
			@Qualifier("batchCoreTransactionManager") PlatformTransactionManager transactionManager,
			StepLoggingListener stepLoggingListener,
			ItemReader<TodayBillInfoBatchEntity.BillInfoItem> todayReceivedBillReader,
			AsyncItemProcessor<TodayBillInfoBatchEntity.BillInfoItem, SummaryRequestStatus> asyncBackgroundSummaryProcessor,
			AsyncItemWriter<SummaryRequestStatus> asyncBackgroundRequestWriter,
			BackgroundRequestWriter backgroundRequestWriter

	) {
		return new StepBuilder(REQUEST_BACKGROUND_SUMMARY_STEP, jobRepository)
				.<TodayBillInfoBatchEntity.BillInfoItem, Future<SummaryRequestStatus>>chunk(chunkSize, transactionManager)
				.reader(todayReceivedBillReader)
				.processor(asyncBackgroundSummaryProcessor)
				.writer(asyncBackgroundRequestWriter)
				.listener(backgroundRequestWriter)
				.listener(stepLoggingListener)
				.faultTolerant()
				.retry(OpenAiException.class)
				.retryLimit(3)
				.backOffPolicy(new ExponentialBackOffPolicy())
				.skip(OpenAiException.class)
				.build();
	}

}
