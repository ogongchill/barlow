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

import com.barlow.app.batch.notificationcenter.NotificationCenterCleanupConstant;
import com.barlow.app.batch.notificationcenter.job.NotificationCenterCleanupBatchRepository;

@Component
@StepScope
public class NotificationCenterCleanupTasklet implements Tasklet {

	private static final Logger log = LoggerFactory.getLogger(NotificationCenterCleanupTasklet.class);

	private final NotificationCenterCleanupBatchRepository repository;

	public NotificationCenterCleanupTasklet(NotificationCenterCleanupBatchRepository repository) {
		this.repository = repository;
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
		LocalDateTime threshold = LocalDateTime.now().minusDays(NotificationCenterCleanupConstant.RETENTION_DAYS);
		int deletedCount = repository.deleteOlderThan(threshold);
		log.info("알림센터 {}일 초과 항목 삭제 완료 - 삭제 건수: {}", NotificationCenterCleanupConstant.RETENTION_DAYS, deletedCount);
		return RepeatStatus.FINISHED;
	}
}
