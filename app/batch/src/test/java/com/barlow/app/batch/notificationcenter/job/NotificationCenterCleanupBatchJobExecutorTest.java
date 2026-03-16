package com.barlow.app.batch.notificationcenter.job;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.barlow.app.batch.BatchCoreContextTest;
import com.barlow.app.batch.BatchCoreTestApplication;
import com.barlow.app.batch.notificationcenter.NotificationCenterConstant;
import com.barlow.support.alert.Alerter;

@SpringBootTest(classes = BatchCoreTestApplication.class)
class NotificationCenterCleanupBatchJobExecutorTest extends BatchCoreContextTest {

	@Autowired
	private NotificationCenterCleanupBatchJobExecutor notificationCenterCleanupBatchJobExecutor;

	@MockBean
	private JobLauncher jobLauncher;

	@MockBean
	private Alerter alerter;

	@Autowired
	@Qualifier(NotificationCenterConstant.JOB_NAME)
	private Job notificationCenterCleanupBatchJob;

	@Test
	@DisplayName("JobLauncher 예외 발생 시 Alerter.alert()가 호출되고 RuntimeException이 re-throw 된다.")
	void notificationCenterCleanupJobExecutor_JobLauncherThrowsException_AlertsAndRethrows() throws Exception {
		// given — JobLauncher가 예외를 던지도록 설정
		given(jobLauncher.run(eq(notificationCenterCleanupBatchJob),
			org.mockito.ArgumentMatchers.any(JobParameters.class)))
			.willThrow(new RuntimeException("배치 실행 실패"));

		// when & then — RuntimeException re-throw 검증
		assertThatThrownBy(() -> notificationCenterCleanupBatchJobExecutor.execute())
			.isInstanceOf(RuntimeException.class);

		// then — Alerter.alert() 호출 검증
		then(alerter).should().alert(anyString());
	}
}
