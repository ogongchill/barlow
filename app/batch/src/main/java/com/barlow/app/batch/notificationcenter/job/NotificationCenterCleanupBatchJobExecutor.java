package com.barlow.app.batch.notificationcenter.job;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.barlow.app.batch.notificationcenter.NotificationCenterCleanupConstant;
import com.barlow.support.alert.Alerter;

@Component
public class NotificationCenterCleanupBatchJobExecutor {

	private static final Logger log = LoggerFactory.getLogger(NotificationCenterCleanupBatchJobExecutor.class);

	private final JobLauncher jobLauncher;
	private final Job job;
	private final Alerter alerter;

	public NotificationCenterCleanupBatchJobExecutor(JobLauncher jobLauncher,
		@Qualifier(NotificationCenterCleanupConstant.JOB_NAME) Job job, Alerter alerter) {
		this.jobLauncher = jobLauncher;
		this.job = job;
		this.alerter = alerter;
	}

	public void execute() {
		JobParameters jobParameters = new JobParameters(
			Map.of("executedAt", new JobParameter<>(UUID.randomUUID().toString(), String.class)));
		try {
			log.info("{} : 알림센터 {}일 초과 항목 삭제 Batch 시작",
				LocalDateTime.now(), NotificationCenterCleanupConstant.RETENTION_DAYS);
			JobExecution jobExecution = jobLauncher.run(job, jobParameters);
			log.info("{} : 알림센터 {}일 초과 항목 삭제 Batch 완료 - {}",
				jobExecution.getEndTime(), NotificationCenterCleanupConstant.RETENTION_DAYS, jobExecution);
		} catch (Exception e) {
			alerter.alert(String.format("알림센터 %d일 초과 항목 삭제 Batch 실패 - %s",
				NotificationCenterCleanupConstant.RETENTION_DAYS, e.getMessage()));
			log.error("{} : 알림센터 {}일 초과 항목 삭제 Batch 실패 - {}",
				LocalDateTime.now(), NotificationCenterCleanupConstant.RETENTION_DAYS, e.getMessage(), e);
		}
	}
}
