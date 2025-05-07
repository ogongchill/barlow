package com.barlow.app.api.controller.v1.legislationaccount;

import com.barlow.core.domain.legislationaccount.LegislationAccount;
import com.barlow.app.support.response.Constant;

public record LegislationAccountProfileResponse(
	String accountName,
	String iconUrl,
	String description,
	int postCount,
	int subscriberCount,
	boolean isSubscribe,
	boolean isNotifiable
) {
	static LegislationAccountProfileResponse from(LegislationAccount account) {
		return new LegislationAccountProfileResponse(
			account.getLegislationType(),
			Constant.IMAGE_ACCESS_URL + account.getIconPath(),
			account.getDescription(),
			account.getPostCount(),
			account.getSubscriberCount(),
			account.isSubscribed(),
			account.isNotifiable()
		);
	}
}
