package com.barlow.batch.core.tracebill.job;

import static com.barlow.batch.core.tracebill.TraceBillConstant.*;

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
public class TrackingBillBatchJobExecutor {

	private static final Logger log = LoggerFactory.getLogger(TrackingBillBatchJobExecutor.class);

	private final JobLauncher jobLauncher;
	private final Job job;
	private final Alerter alerter;

	public TrackingBillBatchJobExecutor(
		JobLauncher jobLauncher,
		@Qualifier(JOB_NAME) Job job,
		Alerter alerter
	) {
		this.jobLauncher = jobLauncher;
		this.job = job;
		this.alerter = alerter;
	}

	public void execute(LocalDate yesterday, LocalDate startDate) {
		JobParameters jobParameters = new JobParameters(Map.of(
			TRACKING_END_DATE_JOB_PARAMETER, new JobParameter<>(yesterday, LocalDate.class),
			TRACKING_START_DATE_JOB_PARAMETER, new JobParameter<>(startDate, LocalDate.class)
		));
		try {
			alerter.alert(String.format("[%s - %s] 법안 상태 추적 Batch 시작", startDate, yesterday));
			log.info("{} : 법안 상태 추적 Batch 시작 [{} - {}]", LocalDateTime.now(), startDate, yesterday);
			JobExecution jobExecution = jobLauncher.run(job, jobParameters);
			log.info("{} : 법안 상태 추적 Batch 완료 - {}", jobExecution.getEndTime(), jobExecution);
		} catch (Exception e) {
			alerter.alert(String.format("🚨법안 상태 추적 Batch 실패 - %s", e.getMessage()));
			log.error("{} : 법안 상태 추적 Batch 실패 - {}", LocalDateTime.now(), e.getMessage(), e);
		}
	}
}
