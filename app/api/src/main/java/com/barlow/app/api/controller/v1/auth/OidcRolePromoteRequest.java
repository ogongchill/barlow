package com.barlow.app.api.controller.v1.auth;

import com.barlow.core.domain.User;
import com.barlow.core.domain.registration.ExternalPrincipal;
import com.barlow.core.domain.registration.MemberPromoteCommand;
import com.barlow.services.auth.authentication.oauth.OidcAuthenticationRequest;

import java.time.LocalDateTime;
import java.util.function.Function;
import java.util.function.Supplier;

public record OidcRolePromoteRequest(
        TermAgreementRequest termAgreementRequest,
        OidcPayload oidcPayload
) {

    MemberPromoteCommand toCommand(
            Supplier<User> userSupplier,
            Function<OidcAuthenticationRequest, ExternalPrincipal> authenticator,
            LocalDateTime submittedAt
    ) {
        return new MemberPromoteCommand(
                authenticator.apply(oidcPayload.toAuthenticationRequest()),
                userSupplier.get(),
                termAgreementRequest.toTermAgreements(submittedAt)
        );
    }
}
