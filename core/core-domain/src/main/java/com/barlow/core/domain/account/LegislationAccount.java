package com.barlow.core.domain.account;

public class LegislationAccount {

    private Long accountNo;
    private String name;
    private String iconUrl;
    private String description;
    private Integer postCount;
    private Integer subscriberCount;

    protected LegislationAccount(Builder builder){
        this.accountNo = builder.accountNo;
        this.name = builder.name;
        this.iconUrl = builder.iconUrl;
        this.description = builder.description;
        this.postCount = builder.postCount;
        this.subscriberCount = builder.subscriberCount;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder<T extends Builder<T>> {
        private Long accountNo;
        private String name;
        private String iconUrl;
        private String description;
        private Integer postCount;
        private Integer subscriberCount;

        public T accountNo(Long accountNo) {
            this.accountNo = accountNo;
            return self();
        }

        public T name(String name) {
            this.name = name;
            return self();
        }

        public T iconUrl(String url) {
            this.iconUrl = url;
            return self();
        }

        public T description(String description) {
            this.description = description;
            return self();
        }

        public T postCount(Integer postCount) {
            this.postCount = postCount;
            return self();
        }

        public T subscriberCount(Integer subscriberCount) {
            this.subscriberCount = subscriberCount;
            return self();
        }

        protected T self() {
            return (T) this;
        }

        public LegislationAccount build() {
            return new LegislationAccount(this);
        }
    }

    public Long getAccountNo() {
        return accountNo;
    }

    public String getName() {
        return name;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public String getDescription() {
        return description;
    }

    public Integer getPostCount() {
        return postCount;
    }

    public Integer getSubscriberCount() {
        return subscriberCount;
    }
}
