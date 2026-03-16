package com.barlow.infra.storage.batch;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.barlow.app.batch.notificationcenter.job.NotificationCenterItemBatchRepository;

@Component
public class NotificationCenterItemBatchRepositoryAdapter implements NotificationCenterItemBatchRepository {

	private final NotificationCenterItemBatchJpaRepository notificationCenterItemBatchJpaRepository;

	public NotificationCenterItemBatchRepositoryAdapter(
		NotificationCenterItemBatchJpaRepository notificationCenterItemBatchJpaRepository) {
		this.notificationCenterItemBatchJpaRepository = notificationCenterItemBatchJpaRepository;
	}

	@Override
	@Transactional
	public int deleteOlderThan(LocalDateTime threshold, int limit) {
		List<Long> ids = notificationCenterItemBatchJpaRepository.findIdsByCreatedAtBefore(threshold,
			PageRequest.of(0, limit));
		if (ids.isEmpty()) {
			return 0;
		}
		notificationCenterItemBatchJpaRepository.deleteAllByIdInBatch(ids);
		return ids.size();
	}
}
