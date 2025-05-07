package com.barlow.app.batch.recentbill.job;

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

import com.barlow.app.batch.recentbill.RecentBillConstant;
import com.barlow.support.alert.Alerter;

@Component
public class RecentBillBatchJobExecutor {

	private static final Logger log = LoggerFactory.getLogger(RecentBillBatchJobExecutor.class);

	private final Alerter alerter;
	private final JobLauncher jobLauncher;
	private final Job job;
	private final Integer chunkSize;

	public RecentBillBatchJobExecutor(
		Alerter alerter,
		JobLauncher jobLauncher,
		@Qualifier(RecentBillConstant.JOB_NAME) Job job,
		@Value("${chunkSize:10}") Integer chunkSize
	) {
		this.alerter = alerter;
		this.jobLauncher = jobLauncher;
		this.job = job;
		this.chunkSize = chunkSize;
	}

	public void execute(LocalDate now) {
		JobParameters jobParameters = new JobParameters(Map.of(
			RecentBillConstant.BATCH_DATE_JOB_PARAMETER, new JobParameter<>(now, LocalDate.class),
			"chunkSize", new JobParameter<>(chunkSize, Integer.class)
		));
		try {
			alerter.alert(String.format("오늘 접수된 법안 게시글 생성 Batch 시작 : %s", LocalDateTime.now()));
			log.info("{} : 오늘 접수된 법안 게시글 생성 Batch 시작", LocalDateTime.now());
			JobExecution jobExecution = jobLauncher.run(job, jobParameters);
			log.info("{} : 오늘 접수된 법안 게시글 생성 Batch 완료 - {}", jobExecution.getEndTime(), jobExecution);
		} catch (Exception e) {
			alerter.alert(String.format("오늘 접수된 법안 게시글 생성 Batch 실패 - %s : %s", e.getMessage(), LocalDateTime.now()));
			log.error("{} : 오늘 접수된 법안 게시글 생성 Batch 실패 - {}", LocalDateTime.now(), e.getMessage(), e);
		}
	}
}
