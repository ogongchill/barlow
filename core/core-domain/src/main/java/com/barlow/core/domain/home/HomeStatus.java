package com.barlow.core.domain.home;

import java.util.List;

public record HomeStatus(
	List<MyLegislationAccount> myLegislationAccounts,
	boolean isNotificationArrived
) {
}
