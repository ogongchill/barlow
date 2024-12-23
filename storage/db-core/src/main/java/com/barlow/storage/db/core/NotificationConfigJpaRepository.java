package com.barlow.storage.db.core;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface NotificationConfigJpaRepository extends JpaRepository<NotificationConfigJpaEntity, Long> {

	@Query("""
		SELECT nc.memberNo, nc.topic, d.deviceOs, d.token FROM NotificationConfigJpaEntity nc
		INNER JOIN DeviceJpaEntity d ON nc.memberNo = d.memberNo
		WHERE nc.topic = :topic AND nc.enable = true AND d.status = 'ACTIVE'""")
	List<NotificationInfoProjection> findAllByEnableTrueAndTopic(NotificationTopic topic);

	@Query("""
		SELECT nc.memberNo, nc.topic, d.deviceOs, d.token FROM NotificationConfigJpaEntity nc
		INNER JOIN DeviceJpaEntity d ON nc.memberNo = d.memberNo
		WHERE nc.topic IN :topics AND nc.enable = true AND d.status = 'ACTIVE'""")
	List<NotificationInfoProjection> findAllByEnableTrueAndTopicIn(Set<NotificationTopic> topics);
}
