package com.barlow.core.domain.registration;

import com.barlow.core.enumerate.AuthProvider;

public record UserAuthProviderQuery(
        Long userNo,
        AuthProvider authProvider
) {
}
