package com.barlow.core.storage.notification;

import java.sql.Types;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import com.barlow.core.enumerate.NotificationTopic;
import com.barlow.services.notification.NotificationCenterItemInfo;
import com.barlow.services.notification.NotificationCenterRepository;

@Component
public class NotificationCenterRepositoryAdapter implements NotificationCenterRepository {

	private final SimpleJdbcInsert simpleJdbcInsert;

	public NotificationCenterRepositoryAdapter(@Qualifier("batchCoreDataSource") DataSource dataSource) {
		this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
			.withTableName("notification_center_item")
			.usingGeneratedKeyColumns("notification_center_item_no");
	}

	@Override
	public void registerAll(List<NotificationCenterItemInfo> notificationCenterItemInfos) {
		SqlParameterSource[] sqlParameterSources = notificationCenterItemInfos.stream()
			.map(this::toSqlParameterSources)
			.flatMap(Arrays::stream)
			.toArray(SqlParameterSource[]::new);
		simpleJdbcInsert.executeBatch(sqlParameterSources);
	}

	private MapSqlParameterSource[] toSqlParameterSources(NotificationCenterItemInfo notificationItemInfo) {
		Long memberNo = notificationItemInfo.memberNo();
		return notificationItemInfo.items().stream()
			.map(billItemInfo -> createSqlParameterSource(memberNo, notificationItemInfo.topic(), billItemInfo))
			.toArray(MapSqlParameterSource[]::new);
	}

	private MapSqlParameterSource createSqlParameterSource(
		Long memberNo,
		NotificationTopic topic,
		NotificationCenterItemInfo.BillItemInfo billItemInfo
	) {
		return new MapSqlParameterSource()
			.addValue("member_no", memberNo)
			.addValue("notification_topic", topic.name())
			.addValue("title", topic.getValue())
			.addValue("body", billItemInfo.billName())
			.addValue("bill_id", billItemInfo.billId())
			.addValue("created_at", LocalDateTime.now(), Types.TIMESTAMP)
			.addValue("updated_at", LocalDateTime.now(), Types.TIMESTAMP);
	}
}
