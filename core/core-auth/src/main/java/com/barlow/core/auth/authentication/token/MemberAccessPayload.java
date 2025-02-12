package com.barlow.core.auth.authentication.token;

public record MemberAccessPayload(
        Long memberNo,
        String role
) {
}
