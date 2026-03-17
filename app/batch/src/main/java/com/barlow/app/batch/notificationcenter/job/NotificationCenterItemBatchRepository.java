package com.barlow.app.batch.notificationcenter.job;

import java.time.LocalDateTime;

public interface NotificationCenterItemBatchRepository {

	int deleteItemsOlderThan(LocalDateTime threshold);
}
