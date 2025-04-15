package com.barlow.batch.core.preannounce.job;

import static com.barlow.batch.core.preannounce.PreAnnounceConstant.BATCH_DATE_JOB_PARAMETER;
import static com.barlow.batch.core.preannounce.PreAnnounceConstant.JOB_NAME;

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
import org.springframework.stereotype.Component;

import com.barlow.support.alert.Alerter;

@Component
public class PreAnnounceBatchJobExecutor {

	private static final Logger log = LoggerFactory.getLogger(PreAnnounceBatchJobExecutor.class);

	private final JobLauncher jobLauncher;
	private final Job job;
	private final Alerter alerter;

	public PreAnnounceBatchJobExecutor(
		JobLauncher jobLauncher,
		@Qualifier(JOB_NAME) Job job,
		Alerter alerter
	) {
		this.jobLauncher = jobLauncher;
		this.job = job;
		this.alerter = alerter;
	}

	public void execute(LocalDate batchDate) {
		JobParameters jobParameters = new JobParameters(Map.of(
			BATCH_DATE_JOB_PARAMETER, new JobParameter<>(batchDate, LocalDate.class)
		));
		try {
			alerter.alert(String.format("진행중인 입법예고 조회 및 업데이트 Batch 시작 : %s", LocalDateTime.now()));
			log.info("{} : 진행중인 입법예고 조회 및 업데이트 Batch 시작", LocalDateTime.now());
			JobExecution jobExecution = jobLauncher.run(job, jobParameters);
			log.info("{} : 진행중인 입법예고 조회 및 업데이트 Batch 완료 - {}", jobExecution.getEndTime(), jobExecution);
		} catch (Exception e) {
			alerter.alert(String.format("🚨진행중인 입법예고 조회 및 업데이트 Batch 실패 - %s : %s", e.getMessage(), LocalDateTime.now()));
			log.error("{} : 진행중인 입법예고 조회 및 업데이트 Batch 실패 - {}", LocalDateTime.now(), e.getMessage(), e);
		}
	}
}
