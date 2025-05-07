package com.barlow.core.domain.home;

import java.util.List;

public record MyHomeStatus(
	List<MyLegislationAccount> myLegislationAccounts,
	boolean isNotificationArrived
) {
}
