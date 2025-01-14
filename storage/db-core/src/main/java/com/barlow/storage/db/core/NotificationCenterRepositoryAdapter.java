package com.barlow.storage.db.core;

import java.sql.Types;
import java.time.LocalDateTime;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import com.barlow.notification.NotificationCenterRepository;
import com.barlow.notification.worker.NotificationCenterInfo;

@Component
public class NotificationCenterRepositoryAdapter implements NotificationCenterRepository {

	private final SimpleJdbcInsert simpleJdbcInsert;

	public NotificationCenterRepositoryAdapter(DataSource dataSource) {
		this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
			.withTableName("notification_center")
			.usingGeneratedKeyColumns("notification_center_no");
	}

	@Override
	public void registerAll(List<NotificationCenterInfo> notificationCenterInfos) {
		SqlParameterSource[] sqlParameterSources = notificationCenterInfos.stream()
			.map(this::createSqlParameterSource)
			.toArray(SqlParameterSource[]::new);
		simpleJdbcInsert.executeBatch(sqlParameterSources);
	}

	private MapSqlParameterSource createSqlParameterSource(NotificationCenterInfo notificationCenterInfo) {
		NotificationTopic topic = NotificationTopic.valueOf(notificationCenterInfo.topic());
		return new MapSqlParameterSource()
			.addValue("member_no", notificationCenterInfo.memberNo())
			.addValue("notification_topic", topic.name())
			.addValue("title", notificationCenterInfo.title())
			.addValue("body", notificationCenterInfo.body())
			.addValue("created_at", LocalDateTime.now(), Types.TIMESTAMP)
			.addValue("updated_at", LocalDateTime.now(), Types.TIMESTAMP);
	}
}
