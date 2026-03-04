package com.barlow.infra.auth.authentication.token;

public record AccessTokenPayload(
	Long memberNo,
	String role
) {
}
