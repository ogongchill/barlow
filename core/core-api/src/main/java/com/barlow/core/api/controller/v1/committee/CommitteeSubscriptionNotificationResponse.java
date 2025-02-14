package com.barlow.core.api.controller.v1.committee;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public record CommitteeSubscriptionNotificationResponse(
	@NotNull String title,
	@NotNull String description,
	@NotNull List<CommitteeSubscriptionNotification> legislations
) {
}