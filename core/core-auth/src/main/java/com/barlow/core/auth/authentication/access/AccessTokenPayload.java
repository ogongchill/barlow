package com.barlow.core.auth.authentication.access;

public record AccessTokenPayload(
        Long memberNo,
        Long issueAt,
        String role
) {

    public static Builder builder() {
        return new Builder();
    }

    public static Builder ofGuest() {
        return builder().role("GUEST");
    }

    public static class Builder {

        private Long memberNo;
        private Long issueAt;
        private String role;

        public Builder memberNo(Long memberNo) {
            this.memberNo = memberNo;
            return this;
        }

        public Builder issueAt(Long issueAt) {
            this.issueAt = issueAt;
            return this;
        }

        public Builder role(String role) {
            this.role = role;
            return this;
        }

        public AccessTokenPayload build() {
            return new AccessTokenPayload(memberNo, issueAt, role);
        }
    }
}
