package com.barlow.core.storage.notification;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.barlow.core.enumerate.NotificationTopic;
import com.barlow.core.storage.NotificationConfigJpaEntity;

public interface NotificationDispatchConfigJpaRepository extends JpaRepository<NotificationConfigJpaEntity, Long> {

	@Query("""
		SELECT new com.barlow.core.storage.notification.NotificationInfoProjection(nc.memberNo, nc.topic, d.deviceOs, d.token)
		FROM NotificationConfigJpaEntity nc
		INNER JOIN DeviceJpaEntity d ON nc.memberNo = d.memberNo
		WHERE nc.topic = :topic AND nc.enable = true AND d.status = 'ACTIVE'""")
	List<NotificationInfoProjection> findAllByEnableTrueAndTopic(@Param("topic") NotificationTopic topic);

	@Query("""
		SELECT new com.barlow.core.storage.notification.NotificationInfoProjection(nc.memberNo, nc.topic, d.deviceOs, d.token)
		FROM NotificationConfigJpaEntity nc
		INNER JOIN DeviceJpaEntity d ON nc.memberNo = d.memberNo
		WHERE nc.topic IN :topics AND nc.enable = true AND d.status = 'ACTIVE'""")
	List<NotificationInfoProjection> findAllByEnableTrueAndTopicIn(@Param("topics") Set<NotificationTopic> topics);
}
