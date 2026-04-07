package com.barlow.app.batch.notificationcenter.job;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.barlow.app.batch.BatchCoreContextTest;
import com.barlow.app.batch.BatchCoreTestApplication;
import com.barlow.infra.storage.batch.NotificationCenterItemBatchRepositoryAdapter;
import com.barlow.support.alert.Alerter;

@SpringBootTest(classes = BatchCoreTestApplication.class)
class NotificationCenterItemBatchRepositoryAdapterTest extends BatchCoreContextTest {

	@Autowired
	private NotificationCenterItemBatchRepositoryAdapter notificationCenterItemBatchRepositoryAdapter;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@MockBean
	private Alerter alerter;

	@Test
	@DisplayName("limit 이하 건수가 threshold 이전에 존재하면 deleteOlderThan 호출 시 실제 삭제된 건수를 반환한다.")
	void deleteOlderThan_LessThanLimitItemsExist_ReturnsActualDeletedCount() {
		// given — threshold 이전 데이터 5건 삽입
		LocalDateTime oldDate = LocalDateTime.now().minusDays(8);
		int insertCount = 5;
		for (int i = 0; i < insertCount; i++) {
			jdbcTemplate.update(
				"INSERT INTO notification_center_item "
					+ "(member_no, bill_id, notification_topic, title, body, created_at, updated_at) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?)",
				10L + i, "BILL_REPO_" + i, "RECEIPT", "title_" + i, "body_" + i, oldDate, oldDate);
		}
		LocalDateTime threshold = LocalDateTime.now().minusDays(7);

		// when — limit(1,000)보다 적은 5건을 대상으로 삭제 실행
		int deleted = notificationCenterItemBatchRepositoryAdapter.deleteOlderThan(threshold, 1000);

		// then — 실제 삭제된 건수(5)가 반환되어야 한다
		assertThat(deleted).isEqualTo(insertCount);

		// then — DB에서도 해당 데이터가 삭제되었음을 검증
		int remainCount = jdbcTemplate.queryForObject(
			"SELECT COUNT(*) FROM notification_center_item WHERE created_at < ?",
			Integer.class, threshold);
		assertThat(remainCount).isZero();
	}

	@Test
	@DisplayName("threshold 이전 데이터가 없으면 deleteOlderThan 호출 시 0을 반환한다.")
	void deleteOlderThan_NoItemsBeforeThreshold_ReturnsZero() {
		// given — 삭제 대상 없음 (threshold 이후 데이터만 존재)
		LocalDateTime threshold = LocalDateTime.now().minusDays(7);

		// when
		int deleted = notificationCenterItemBatchRepositoryAdapter.deleteOlderThan(threshold, 1000);

		// then
		assertThat(deleted).isZero();
	}
}
