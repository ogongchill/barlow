package com.barlow.core.auth.authentication.access;

import com.barlow.core.auth.authentication.core.AuthenticationException;
import com.barlow.core.auth.authentication.core.AuthenticationExceptionType;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class AccessTokenException extends AuthenticationException {

    public AccessTokenException(String message, AuthenticationExceptionType authenticationExceptionType) {
        super(message, authenticationExceptionType);
    }

    public static AccessTokenException expired(long expiration) {
        LocalDateTime dateTime = Instant.ofEpochMilli(expiration)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        return new AccessTokenException("토큰 만료됨 exp:" + dateTime, AuthenticationExceptionType.EXPIRED_CREDENTIAL);
    }

    public static AccessTokenException invalid() {
        return new AccessTokenException("유효하지 않은 토큰", AuthenticationExceptionType.INVALID_CREDENTIAL);
    }
}
