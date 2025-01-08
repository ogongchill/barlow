package com.barlow.core.domain.notification;

import org.jetbrains.annotations.NotNull;

public record NotificationConfig(
        @NotNull Long no,
        @NotNull Long memberNo,
        @NotNull Boolean isEnable,
        @NotNull NotifiableTopic topic
) {

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Long no;
        private Long memberNo;
        private Boolean isEnable;
        private NotifiableTopic topic;

        public Builder no(Long no) {
            this.no = no;
            return this;
        }

        public Builder memberNo(Long memberNo) {
            this.memberNo = no;
            return this;
        }

        public Builder isEnable(Boolean isEnable) {
            this.isEnable = isEnable;
            return this;
        }

        public Builder topic(NotifiableTopic topic) {
            this.topic = topic;
            return this;
        }

        public NotificationConfig build() {
            return new NotificationConfig(
                    this.no,
                    this.memberNo,
                    this.isEnable,
                    this.topic
            );
        }
    }
}
