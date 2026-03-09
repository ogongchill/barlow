package com.barlow.infra.storage.notification;

import java.time.LocalDateTime;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import com.barlow.app.batch.notificationcenter.job.NotificationCenterCleanupBatchRepository;

@Component
public class NotificationCenterCleanupBatchRepositoryAdapter implements NotificationCenterCleanupBatchRepository {

	private final NamedParameterJdbcTemplate jdbcTemplate;

	public NotificationCenterCleanupBatchRepositoryAdapter(
		@Qualifier("batchCoreDataSource") DataSource dataSource) {
		this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}

	@Override
	public int deleteOlderThan(LocalDateTime threshold) {
		String sql = "DELETE FROM notification_center_item WHERE created_at < :threshold";
		return jdbcTemplate.update(sql, new MapSqlParameterSource("threshold", threshold));
	}
}
