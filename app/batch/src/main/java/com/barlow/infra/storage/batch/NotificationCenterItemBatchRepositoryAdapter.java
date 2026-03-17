package com.barlow.infra.storage.batch;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.barlow.app.batch.notificationcenter.job.NotificationCenterItemBatchRepository;

@Component
public class NotificationCenterItemBatchRepositoryAdapter implements NotificationCenterItemBatchRepository {

	private final NotificationCenterItemBatchJpaRepository notificationCenterItemBatchJpaRepository;

	public NotificationCenterItemBatchRepositoryAdapter(
		NotificationCenterItemBatchJpaRepository notificationCenterItemBatchJpaRepository) {
		this.notificationCenterItemBatchJpaRepository = notificationCenterItemBatchJpaRepository;
	}

	@Override
	public int deleteItemsOlderThan(LocalDateTime threshold) {
		return notificationCenterItemBatchJpaRepository.deleteByCreatedAtBefore(threshold);
	}
}
