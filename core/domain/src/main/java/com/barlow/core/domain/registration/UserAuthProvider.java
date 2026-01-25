package com.barlow.core.domain.registration;

import java.util.List;

public record UserAuthProvider(
        Long userNo,
        List<ExternalPrincipal> externalPrincipals
) {
}
