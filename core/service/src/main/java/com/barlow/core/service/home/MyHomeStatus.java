package com.barlow.core.service.home;

import java.util.List;

import com.barlow.core.domain.legislationaccount.MyLegislationAccount;

public record MyHomeStatus(List<MyLegislationAccount> myLegislationAccounts, boolean isNotificationArrived) {
}
