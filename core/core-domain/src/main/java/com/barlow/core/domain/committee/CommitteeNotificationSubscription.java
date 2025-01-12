package com.barlow.core.domain.committee;

import org.jetbrains.annotations.NotNull;

public record CommitteeNotificationSubscription(
        @NotNull Long legislationAccountNo,
        @NotNull String name,
        @NotNull String iconUrl,
        @NotNull Boolean isSubscriptionEnable,
        @NotNull Boolean isNotificationEnable
) {

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Long legislationAccountNo;
        private String name;
        private String iconUrl;
        private Boolean isSubscriptionEnable;
        private Boolean isNotificationEnable;

        public Builder legislationAccountNo(Long legislationAccountNo) {
            this.legislationAccountNo = legislationAccountNo;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder iconUrl(String iconUrl) {
            this.iconUrl = iconUrl;
            return this;
        }

        public Builder isSubscriptionEnable(Boolean isSubscriptionEnable) {
            this.isSubscriptionEnable = isSubscriptionEnable;
            return this;
        }

        public Builder isNotificationEnable(Boolean isNotificationEnable) {
            this.isNotificationEnable = isNotificationEnable;
            return this;
        }

        public CommitteeNotificationSubscription build() {
            return new CommitteeNotificationSubscription(
                    legislationAccountNo,
                    name,
                    iconUrl,
                    isSubscriptionEnable,
                    isNotificationEnable
            );
        }
    }
}
