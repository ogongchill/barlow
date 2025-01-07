package com.barlow.core.domain.account;

import org.jetbrains.annotations.NotNull;

public record LegislationAccount(
        @NotNull Long no,
        @NotNull String name,
        @NotNull String iconUrl,
        @NotNull String description,
        @NotNull Integer postCount,
        @NotNull Integer subscriberCount
) {

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Long no;
        private String name;
        private String iconUrl;
        private String description;
        private Integer subscriberCount;
        private Integer postCount;

        public Builder no(Long no) {
            this.no = no;
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

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder subscriberCount(Integer subscriberCount) {
            this.subscriberCount = subscriberCount;
            return this;
        }

        public Builder postCount(Integer postCount) {
            this.postCount = postCount;
            return this;
        }

        public LegislationAccount build() {
            return new LegislationAccount(
                    this.no,
                    this.name,
                    this.iconUrl,
                    this.description,
                    this.subscriberCount,
                    this.postCount
            );
        }
    }
}
