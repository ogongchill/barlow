package com.barlow.core.storage;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.barlow.core.enumerate.NotificationTopic;

public interface NotificationConfigJpaRepository extends JpaRepository<NotificationConfigJpaEntity, Long> {

	List<NotificationConfigJpaEntity> findAllByMemberNo(Long memberNo);

	NotificationConfigJpaEntity findByTopicAndMemberNo(NotificationTopic topic, Long memberNo);

	@Modifying
	@Query("DELETE FROM NotificationConfigJpaEntity nc WHERE nc.topic = :topic AND nc.memberNo = :memberNo")
	void deleteByTopicAndMemberNo(@Param("topic") NotificationTopic topic, @Param("memberNo") Long memberNo);
}
