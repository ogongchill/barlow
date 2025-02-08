package com.barlow.core.auth.authentication.token;

import org.springframework.stereotype.Component;

@Component
public interface MemberTokenValidator {

    TokenValidationResult<MemberAccessPayload> validate(Token token);
}
