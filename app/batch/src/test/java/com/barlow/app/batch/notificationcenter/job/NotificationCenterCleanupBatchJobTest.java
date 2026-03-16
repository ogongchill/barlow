package com.barlow.app.batch.notificationcenter.job;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.barlow.app.batch.BatchCoreContextTest;
import com.barlow.app.batch.BatchCoreTestApplication;
import com.barlow.app.batch.notificationcenter.NotificationCenterConstant;
import com.barlow.support.alert.Alerter;

@SpringBatchTest
@SpringBootTest(classes = BatchCoreTestApplication.class)
class NotificationCenterCleanupBatchJobTest extends BatchCoreContextTest {

	@Autowired
	private JobLauncherTestUtils jobLauncherTestUtils;

	@Autowired
	@Qualifier(NotificationCenterConstant.JOB_NAME)
	private Job notificationCenterCleanupBatchJob;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@MockBean
	private Alerter alerter;

	@BeforeEach
	void setUp() {
		jobLauncherTestUtils.setJob(notificationCenterCleanupBatchJob);
	}

	@Test
	@DisplayName("threshold 이전 알림 데이터가 2,500건 있으면 배치 실행 시 전체 삭제 후 COMPLETED 반환된다.")
	void notificationCenterCleanupJob_2500OldItemsExist_DeletesAllAndCompletes() throws Exception {
		// given — 7일 초과 데이터 2,500건 삽입
		LocalDateTime oldDate = LocalDateTime.now().minusDays(8);
		for (int i = 0; i < 2500; i++) {
			jdbcTemplate.update(
				"INSERT INTO notification_center_item "
					+ "(member_no, bill_id, notification_topic, title, body, created_at, updated_at) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?)",
				1L, "BILL_ID_" + i, "RECEIPT", "title_" + i, "body_" + i, oldDate, oldDate);
		}
		int beforeCount = countOldItems(oldDate.plusSeconds(1));
		assertThat(beforeCount).isEqualTo(2500);

		// when — Job 실행
		JobExecution jobExecution = jobLauncherTestUtils.launchJob();

		// then — ExitStatus 검증
		assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);

		// then — 7일 초과 데이터 전체 삭제 검증
		int afterCount = countOldItems(LocalDateTime.now().minusDays(7));
		assertThat(afterCount).isZero();
	}

	@Test
	@DisplayName("삭제 대상 알림 데이터가 0건이면 배치 실행 시 삭제 없이 COMPLETED 반환된다.")
	void notificationCenterCleanupJob_NoOldItemsExist_CompletesWithoutDeletion() throws Exception {
		// given — 7일 이내 데이터만 존재 (삭제 대상 없음)
		LocalDateTime recentDate = LocalDateTime.now().minusDays(3);
		jdbcTemplate.update(
			"INSERT INTO notification_center_item "
				+ "(member_no, bill_id, notification_topic, title, body, created_at, updated_at) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?)",
			2L, "RECENT_BILL_001", "RECEIPT", "최근 알림", "내용", recentDate, recentDate);

		// when — Job 실행
		JobExecution jobExecution = jobLauncherTestUtils.launchJob();

		// then — ExitStatus 검증
		assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);

		// then — 7일 이내 데이터는 삭제되지 않음
		int remainCount = jdbcTemplate.queryForObject(
			"SELECT COUNT(*) FROM notification_center_item WHERE member_no = ?",
			Integer.class, 2L);
		assertThat(remainCount).isEqualTo(1);
	}

	private int countOldItems(LocalDateTime threshold) {
		return jdbcTemplate.queryForObject(
			"SELECT COUNT(*) FROM notification_center_item WHERE created_at < ?",
			Integer.class, threshold);
	}
}
