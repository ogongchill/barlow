package com.barlow.storage.db.core;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.barlow.core.enumerate.NotificationTopic;

public interface NotificationConfigJpaRepository extends JpaRepository<NotificationConfigJpaEntity, Long> {

	List<NotificationConfigJpaEntity> findAllByMemberNo(Long memberNo);

	NotificationConfigJpaEntity findByTopicAndMemberNo(NotificationTopic topic, Long memberNo);

	@Transactional
	void deleteByTopicAndMemberNo(NotificationTopic topic, Long memberNo);
}
