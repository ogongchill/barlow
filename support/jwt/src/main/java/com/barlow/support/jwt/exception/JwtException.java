package com.barlow.support.jwt.exception;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.fasterxml.jackson.core.JsonProcessingException;

public class JwtException extends RuntimeException {

    public JwtException(String message, Throwable cause) {
        super(message, cause);
    }

    public JwtException(String message) {
        super(message);
    }

    public static JwtException algorithmMismatch(AlgorithmMismatchException e) {
        return new JwtException(e.getMessage(), e);
    }

    public static JwtException cannotParsePayload(JsonProcessingException e) {
        return new JwtException(e.getMessage(), e);
    }
}
