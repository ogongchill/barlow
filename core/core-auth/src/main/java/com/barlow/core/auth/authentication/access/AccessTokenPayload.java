package com.barlow.core.auth.authentication.access;

public record AccessTokenPayload(
        Long memberNo,
        String role
) {
}
