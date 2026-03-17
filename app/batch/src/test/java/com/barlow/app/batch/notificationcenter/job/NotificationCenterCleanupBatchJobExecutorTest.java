package com.barlow.app.batch.notificationcenter.job;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import com.barlow.app.batch.BatchCoreContextTest;
import com.barlow.app.batch.BatchCoreTestApplication;
import com.barlow.support.alert.Alerter;

@SpringBootTest(classes = BatchCoreTestApplication.class)
@TestPropertySource(properties = {
	"KNAL_OPEN_DATA_API_SERVICE_KEY=mock-key",
	"KNAL_OPEN_CONGRESS_API_SERVICE_KEY=mock-key",
	"SUPPORT_ALERT_SLACK_WEBHOOK_URL=https://hooks.slack.com/services/mock-test-url"
})
class NotificationCenterCleanupBatchJobExecutorTest extends BatchCoreContextTest {

	@MockBean
	private JobLauncher jobLauncher;

	@MockBean
	private Alerter alerter;

	private final NotificationCenterCleanupBatchJobExecutor executor;

	NotificationCenterCleanupBatchJobExecutorTest(
		NotificationCenterCleanupBatchJobExecutor executor) {
		this.executor = executor;
	}

	@Test
	@DisplayName("배치 잡 실행 중 예외가 발생하면 alerter.alert()가 호출된다.")
	void notificationCenterCleanupBatchJobExecutor_JobLauncherThrowsException_CallsAlerter() throws Exception {
		// given — JobLauncher.run() 호출 시 예외 발생하도록 설정
		doThrow(new RuntimeException("배치 실행 오류")).when(jobLauncher).run(any(), any());

		// when
		executor.execute();

		// then — alerter.alert() 가 호출되었는지 확인
		verify(alerter).alert(anyString());
	}

	@Test
	@DisplayName("배치 잡 실행이 성공하면 alerter.alert()가 호출되지 않는다.")
	void notificationCenterCleanupBatchJobExecutor_JobSucceeds_DoesNotCallAlerter() throws Exception {
		// given — JobLauncher.run() 정상 실행 (mock 기본 반환 null)

		// when
		executor.execute();

		// then — alerter.alert() 가 호출되지 않아야 함
		verify(alerter, never()).alert(anyString());
	}
}
