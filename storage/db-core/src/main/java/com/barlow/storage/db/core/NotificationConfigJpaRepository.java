package com.barlow.storage.db.core;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.barlow.core.enumerate.NotificationTopic;

public interface NotificationConfigJpaRepository extends JpaRepository<NotificationConfigJpaEntity, Long> {

	List<NotificationConfigJpaEntity> findAllByMemberNo(Long memberNo);

	NotificationConfigJpaEntity findByTopicAndMemberNo(NotificationTopic topic, Long memberNo);

	void deleteByTopicAndMemberNo(NotificationTopic topic, Long memberNo);
}
