package com.barlow.core.auth.authentication.token;

public record MemberAccessPayload(
        Long memberNo,
        String role
) {
    public static MemberAccessPayload invalid() {
        return new MemberAccessPayload(-1L, "");
    }
}
