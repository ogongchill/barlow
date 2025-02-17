package com.barlow.core.auth.authentication.token;

public record AccessTokenPayload(
        Long memberNo,
        String role
) {

}
