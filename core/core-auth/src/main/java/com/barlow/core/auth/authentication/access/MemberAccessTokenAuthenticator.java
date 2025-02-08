package com.barlow.core.auth.authentication.access;

import com.barlow.core.auth.authentication.core.Authenticator;
import com.barlow.core.auth.authentication.token.*;
import org.springframework.stereotype.Component;

@Component
public class MemberAccessTokenAuthenticator implements Authenticator<Token, MemberPrincipal> {

    private final MemberTokenValidator memberTokenValidator;

    public MemberAccessTokenAuthenticator(MemberTokenValidator memberTokenValidator) {
        this.memberTokenValidator = memberTokenValidator;
    }

    @Override
    public MemberPrincipal authenticate(Token token) {
        TokenValidationResult<MemberAccessPayload> result = memberTokenValidator.validate(token);
        if(result.getType().equals(TokenValidationResultType.SUCCESS)) {
            MemberAccessPayload payload = result.getPayload();
            return new MemberPrincipal(payload.memberNo(), payload.role());
        }
        //예외처리 추가해야함
        throw new IllegalArgumentException();
    }
}

