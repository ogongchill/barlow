package com.barlow.core.api.controller.v1.committee;

import org.jetbrains.annotations.NotNull;

public record CommitteeSubscriptionNotification(
	@NotNull Long legislationAccountNo,
	@NotNull String accountName,
	@NotNull String iconUrl,
	@NotNull Boolean isSubscriptionEnabled,
	@NotNull Boolean isNotificationEnabled
) {

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {

		private Long legislationAccountNo;
		private String accountName;
		private String iconUrl;
		private Boolean isSubscriptionEnabled;
		private Boolean isNotificationEnabled;

		public Builder legislationAccountNo(Long legislationAccountNo) {
			this.legislationAccountNo = legislationAccountNo;
			return this;
		}

		public Builder accountName(String accountName) {
			this.accountName = accountName;
			return this;
		}

		public Builder iconUrl(String iconUrl) {
			this.iconUrl = iconUrl;
			return this;
		}

		public Builder isSubscriptionEnabled(Boolean isSubscriptionEnabled) {
			this.isSubscriptionEnabled = isSubscriptionEnabled;
			return this;
		}

		public Builder isNotificationEnabled(Boolean isNotificationEnabled) {
			this.isNotificationEnabled = isNotificationEnabled;
			return this;
		}

		public CommitteeSubscriptionNotification build() {
			return new CommitteeSubscriptionNotification(
				legislationAccountNo,
				accountName,
				iconUrl,
				isSubscriptionEnabled,
				isNotificationEnabled
			);
		}
	}
}
