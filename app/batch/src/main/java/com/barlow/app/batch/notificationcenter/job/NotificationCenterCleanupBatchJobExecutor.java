package com.barlow.app.batch.notificationcenter.job;

import static com.barlow.app.batch.notificationcenter.NotificationCenterConstant.JOB_NAME;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.barlow.support.alert.Alerter;

@Component
public class NotificationCenterCleanupBatchJobExecutor {

	private static final Logger log = LoggerFactory.getLogger(NotificationCenterCleanupBatchJobExecutor.class);

	private final JobLauncher jobLauncher;
	private final Job notificationCenterCleanupJob;
	private final Alerter alerter;

	public NotificationCenterCleanupBatchJobExecutor(JobLauncher jobLauncher,
		@Qualifier(JOB_NAME) Job notificationCenterCleanupJob, Alerter alerter) {
		this.jobLauncher = jobLauncher;
		this.notificationCenterCleanupJob = notificationCenterCleanupJob;
		this.alerter = alerter;
	}

	public void execute() {
		try {
			JobParameters params = new JobParametersBuilder()
				.addLocalDateTime("requestDateTime", LocalDateTime.now())
				.toJobParameters();
			jobLauncher.run(notificationCenterCleanupJob, params);
		} catch (Exception e) {
			log.error("[NotificationCenterCleanupBatchJobExecutor] 배치 실행 실패", e);
			alerter.alert(e.getMessage());
		}
	}
}
