package com.barlow.core.auth.authentication.id;

import com.barlow.core.auth.authentication.core.AuthenticationException;
import com.barlow.core.auth.authentication.core.AuthenticationExceptionType;

public class MemberNoAuthenticationException extends AuthenticationException {

    public MemberNoAuthenticationException(String message, AuthenticationExceptionType type) {
        super(message, type);
    }

    public static MemberNoAuthenticationException memberNotFound(long memberNo) {
        return new MemberNoAuthenticationException(String.format("%d 사용자 조회 불가", memberNo), AuthenticationExceptionType.INVALID_CREDENTIAL);
    }
}
