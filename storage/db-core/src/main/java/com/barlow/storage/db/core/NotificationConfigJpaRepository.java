package com.barlow.storage.db.core;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NotificationConfigJpaRepository extends JpaRepository<NotificationConfigJpaEntity, Long> {

	@Query("""
		SELECT new com.barlow.storage.db.core.NotificationInfoProjection(nc.memberNo, nc.topic, d.deviceOs, d.token)
		FROM NotificationConfigJpaEntity nc
		INNER JOIN DeviceJpaEntity d ON nc.memberNo = d.memberNo
		WHERE nc.topic = :topic AND nc.enable = true AND d.status = 'ACTIVE'""")
	List<NotificationInfoProjection> findAllByEnableTrueAndTopic(@Param("topic") NotificationTopic topic);

	@Query("""
		SELECT new com.barlow.storage.db.core.NotificationInfoProjection(nc.memberNo, nc.topic, d.deviceOs, d.token)
		FROM NotificationConfigJpaEntity nc
		INNER JOIN DeviceJpaEntity d ON nc.memberNo = d.memberNo
		WHERE nc.topic IN :topics AND nc.enable = true AND d.status = 'ACTIVE'""")
	List<NotificationInfoProjection> findAllByEnableTrueAndTopicIn(@Param("topics") Set<NotificationTopic> topics);

	@Query("""
		SELECT NotificationConfigJpaEntity d
		FROM NotificationConfigJpaEntity nc
		WHERE nc.memberNo = :memberNo""")
	List<NotificationConfigJpaEntity> findAllByMemberNo(Long memberNo);
}
