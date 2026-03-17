package com.barlow.app.batch.notificationcenter.job.step;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import com.barlow.app.batch.notificationcenter.job.NotificationCenterItemBatchRepository;

@Component
@StepScope
public class NotificationCenterCleanupTasklet implements Tasklet {

	private static final Logger log = LoggerFactory.getLogger(NotificationCenterCleanupTasklet.class);
	private static final int RETENTION_DAYS = 7;

	private final NotificationCenterItemBatchRepository notificationCenterItemBatchRepository;

	public NotificationCenterCleanupTasklet(
		NotificationCenterItemBatchRepository notificationCenterItemBatchRepository) {
		this.notificationCenterItemBatchRepository = notificationCenterItemBatchRepository;
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
		LocalDateTime threshold = LocalDateTime.now().minusDays(RETENTION_DAYS);
		int deletedCount = notificationCenterItemBatchRepository.deleteItemsOlderThan(threshold);
		log.info("[NotificationCenterCleanupTasklet] 7일 초과 알림 삭제 완료 — 삭제 건수: {}", deletedCount);
		return RepeatStatus.FINISHED;
	}
}
