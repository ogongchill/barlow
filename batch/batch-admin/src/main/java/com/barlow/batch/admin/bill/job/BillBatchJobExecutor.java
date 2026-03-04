package com.barlow.batch.admin.bill.job;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.barlow.batch.admin.bill.BillConstant;

@Component
public class BillBatchJobExecutor {

	private static final Logger log = LoggerFactory.getLogger(BillBatchJobExecutor.class);

	private final JobLauncher jobLauncher;
	private final Job job;
	private final Integer chunkSize;

	public BillBatchJobExecutor(
		JobLauncher jobLauncher,
		@Qualifier("billCreateBatchJob") Job job,
		@Value("${chunkSize:100}") Integer chunkSize
	) {
		this.jobLauncher = jobLauncher;
		this.job = job;
		this.chunkSize = chunkSize;
	}

	public void execute(LocalDate start, LocalDate end) {
		JobParameters jobParameters = new JobParameters(Map.of(
			BillConstant.START_DATE_JOB_PARAMETER, new JobParameter<>(start, LocalDate.class),
			BillConstant.END_DATE_JOB_PARAMETER, new JobParameter<>(end, LocalDate.class),
			"chunkSize", new JobParameter<>(chunkSize, Integer.class)
		));
		try {
			log.info("{} : 법안 게시글 생성 Batch 시작", LocalDateTime.now());
			JobExecution jobExecution = jobLauncher.run(job, jobParameters);
			log.info("{} : 법안 게시글 생성 Batch 완료 - {}", jobExecution.getEndTime(), jobExecution);
		} catch (Exception e) {
			log.error("{} : 법안 게시글 생성 Batch 실패 - {}", LocalDateTime.now(), e.getMessage(), e);
		}
	}
}
