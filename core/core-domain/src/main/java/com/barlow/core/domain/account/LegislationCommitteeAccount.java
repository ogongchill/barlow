package com.barlow.core.domain.account;

public class LegislationCommitteeAccount extends LegislationAccount{

    private String committeeCode;
    private Boolean isStandingCommittee;

    public LegislationCommitteeAccount(Builder builder) {
        super(builder);
        this.committeeCode = builder.committeeCode;
        this.isStandingCommittee = builder.isStandingCommittee;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends LegislationAccount.Builder<Builder> {

        private String committeeCode;
        private Boolean isStandingCommittee;

        public Builder committeeCode(String committeeCode) {
            this.committeeCode = committeeCode;
            return this;
        }

        public Builder isStandingCommittee(Boolean isStandingCommittee) {
            this.isStandingCommittee = isStandingCommittee;
            return this;
        }

        @Override
        public LegislationCommitteeAccount build() {
            return new LegislationCommitteeAccount(this);
        }

        @Override
        protected Builder self() {
            return this;
        }
    }

    public String getCommitteeCode() {
        return committeeCode;
    }

    public Boolean getStandingCommittee() {
        return isStandingCommittee;
    }
}
