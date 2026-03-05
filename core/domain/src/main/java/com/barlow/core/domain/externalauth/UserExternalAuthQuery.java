package com.barlow.core.domain.externalauth;

import com.barlow.core.enumerate.AuthProvider;

public record UserExternalAuthQuery(Long userNo, AuthProvider authProvider) {
}
