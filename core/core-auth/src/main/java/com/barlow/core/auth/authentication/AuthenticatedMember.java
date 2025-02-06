package com.barlow.core.auth.authentication;

public record AuthenticatedMember(
        String memberNo,
        String role
) {
}
