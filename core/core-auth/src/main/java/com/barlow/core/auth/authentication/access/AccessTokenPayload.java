package com.barlow.core.auth.authentication.access;

public record AccessTokenPayload(
        Long memberNo,
        String role
) {

    public static AccessTokenPayload ofGuest(Long memberNo) {
        return new AccessTokenPayload(memberNo, "GUEST");
    }
}
