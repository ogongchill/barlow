package com.barlow.batch.core.recentbill.job;

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

import com.barlow.batch.core.recentbill.client.NationalAssemblyLegislationClient;
import com.barlow.batch.core.recentbill.client.TodayBillInfoResult;

@Component
public class RecentBillBatchJobExecutor {

	private static final Logger log = LoggerFactory.getLogger(RecentBillBatchJobExecutor.class);

	private final JobLauncher jobLauncher;
	private final Job job;
	private final NationalAssemblyLegislationClient client;
	private final Integer chunkSize;

	public RecentBillBatchJobExecutor(
		JobLauncher jobLauncher,
		@Qualifier("todayBillCreateBatchJob") Job job,
		NationalAssemblyLegislationClient client,
		@Value("${chunkSize:10}") Integer chunkSize
	) {
		this.jobLauncher = jobLauncher;
		this.job = job;
		this.client = client;
		this.chunkSize = chunkSize;
	}

	void execute(LocalDate now) {
		log.info("{} : 오늘 접수된 법안 호출", LocalDateTime.now());
		TodayBillInfoResult todayBillInfo = client.getTodayBillInfo(now, null, null, null);
		log.info("{} : 오늘 접수된 법안 조회 완료", LocalDateTime.now());

		JobParameters jobParameters = new JobParameters(Map.of(
			"batchDate", new JobParameter<>(now, LocalDate.class),
			"chunkSize", new JobParameter<>(chunkSize, Integer.class),
			"todayBillInfo", new TodayBillInfoJobParameter(todayBillInfo)
		));
		try {
			log.info("{} : 오늘 접수된 법안 게시글 생성 Batch 시작", LocalDateTime.now());
			JobExecution jobExecution = jobLauncher.run(job, jobParameters);
			log.info("{} : 오늘 접수된 법안 게시글 생성 Batch 완료 - {}", jobExecution.getEndTime(), jobExecution);
		} catch (Exception e) {
			log.error("{} : 오늘 접수된 법안 게시글 생성 Batch 실패 - {}", LocalDateTime.now(), e.getMessage(), e);
		}
	}
}
