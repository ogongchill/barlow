package com.barlow.services.auth.authentication.token;

public record AccessTokenPayload(
	Long memberNo,
	String role
) {
}
