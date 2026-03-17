package com.barlow.app.batch.notificationcenter.job;

import static com.barlow.app.batch.notificationcenter.NotificationCenterConstant.JOB_NAME;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;

import com.barlow.app.batch.BatchCoreContextTest;
import com.barlow.app.batch.BatchCoreTestApplication;
import com.barlow.support.alert.Alerter;

@SpringBatchTest
@SpringBootTest(classes = BatchCoreTestApplication.class)
@TestPropertySource(properties = {
	"KNAL_OPEN_DATA_API_SERVICE_KEY=mock-key",
	"KNAL_OPEN_CONGRESS_API_SERVICE_KEY=mock-key",
	"SUPPORT_ALERT_SLACK_WEBHOOK_URL=https://hooks.slack.com/services/mock-test-url"
})
class NotificationCenterCleanupBatchJobTest extends BatchCoreContextTest {

	@MockBean
	@SuppressWarnings("unused")
	private Alerter alerter;

	private final JobLauncherTestUtils jobLauncherTestUtils;
	private final JdbcTemplate jdbcTemplate;

	NotificationCenterCleanupBatchJobTest(
		JobLauncherTestUtils jobLauncherTestUtils,
		JdbcTemplate jdbcTemplate,
		@Qualifier(JOB_NAME) Job notificationCenterCleanupJob) {
		this.jobLauncherTestUtils = jobLauncherTestUtils;
		this.jdbcTemplate = jdbcTemplate;
		this.jobLauncherTestUtils.setJob(notificationCenterCleanupJob);
	}

	@BeforeEach
	void cleanUp() {
		jdbcTemplate.update("DELETE FROM notification_center_item");
	}

	@Test
	@DisplayName("7일 초과 알림센터 항목이 있으면 배치 실행 시 삭제된다.")
	void notificationCenterCleanupJob_OldItemsExist_DeletesExpiredItems() throws Exception {
		// given — 8일 전 항목 2건, 6일 전 항목 1건 삽입
		LocalDateTime eightDaysAgo = LocalDateTime.now().minusDays(8);
		LocalDateTime sixDaysAgo = LocalDateTime.now().minusDays(6);

		jdbcTemplate.update(
			"INSERT INTO notification_center_item (member_no, bill_id, notification_topic, title, body, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?)",
			1L, "BILL_001", "RECEIPT", "8일전 알림 제목 1", "8일전 알림 내용 1", eightDaysAgo, eightDaysAgo);
		jdbcTemplate.update(
			"INSERT INTO notification_center_item (member_no, bill_id, notification_topic, title, body, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?)",
			2L, "BILL_002", "RECEIPT", "8일전 알림 제목 2", "8일전 알림 내용 2", eightDaysAgo, eightDaysAgo);
		jdbcTemplate.update(
			"INSERT INTO notification_center_item (member_no, bill_id, notification_topic, title, body, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?)",
			3L, "BILL_003", "RECEIPT", "6일전 알림 제목", "6일전 알림 내용", sixDaysAgo, sixDaysAgo);

		// when
		JobExecution jobExecution = jobLauncherTestUtils.launchJob();

		// then — 배치 COMPLETED
		assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);

		// then — 7일 초과(8일전) 항목 2건 삭제, 6일전 항목 1건 유지
		Integer remainingCount = jdbcTemplate.queryForObject(
			"SELECT COUNT(*) FROM notification_center_item", Integer.class);
		assertThat(remainingCount).isEqualTo(1);

		Integer sixDaysCount = jdbcTemplate.queryForObject(
			"SELECT COUNT(*) FROM notification_center_item WHERE bill_id = ?", Integer.class, "BILL_003");
		assertThat(sixDaysCount).isEqualTo(1);
	}

	@Test
	@DisplayName("7일 초과 알림센터 항목이 없으면 배치 실행 시 삭제 없이 COMPLETED 된다.")
	void notificationCenterCleanupJob_NoExpiredItems_CompletesWithoutDeletion() throws Exception {
		// given — 6일 전 항목만 존재
		LocalDateTime sixDaysAgo = LocalDateTime.now().minusDays(6);

		jdbcTemplate.update(
			"INSERT INTO notification_center_item (member_no, bill_id, notification_topic, title, body, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?)",
			1L, "BILL_010", "RECEIPT", "6일전 알림 제목", "6일전 알림 내용", sixDaysAgo, sixDaysAgo);

		// when
		JobExecution jobExecution = jobLauncherTestUtils.launchJob();

		// then — 배치 COMPLETED
		assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);

		// then — 삭제 없이 그대로 유지
		Integer remainingCount = jdbcTemplate.queryForObject(
			"SELECT COUNT(*) FROM notification_center_item WHERE bill_id = ?", Integer.class, "BILL_010");
		assertThat(remainingCount).isEqualTo(1);
	}
}
