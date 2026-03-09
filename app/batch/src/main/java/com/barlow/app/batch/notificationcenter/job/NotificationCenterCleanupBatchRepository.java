package com.barlow.app.batch.notificationcenter.job;

import java.time.LocalDateTime;

public interface NotificationCenterCleanupBatchRepository {

	int deleteOlderThan(LocalDateTime threshold);
}
