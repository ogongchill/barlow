package com.barlow.app.batch.notificationcenter.job;

import java.time.LocalDateTime;

public interface NotificationCenterItemBatchRepository {
	int deleteOlderThan(LocalDateTime threshold, int limit);
}
