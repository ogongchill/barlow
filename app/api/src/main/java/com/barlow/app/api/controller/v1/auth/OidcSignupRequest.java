package com.barlow.app.api.controller.v1.auth;

import com.barlow.core.domain.account.authprovider.ExternalPrincipal;
import com.barlow.core.domain.account.create.MemberCreateCommand;
import com.barlow.core.enumerate.DeviceOs;
import com.barlow.services.auth.authentication.oauth.OidcAuthenticationRequest;

import java.time.LocalDateTime;
import java.util.function.Function;

public record OidcSignupRequest(
        OidcPayload oidcPayload,
        TermAgreementRequest termAgreementRequest,
        SignupRequest signupRequest
) {
    public MemberCreateCommand.UserPayload toPayload() {
        return new MemberCreateCommand.UserPayload(
                DeviceOs.valueOf(signupRequest.deviceOs().toUpperCase()),
                signupRequest().deviceId(),
                signupRequest().deviceToken(),
                signupRequest.nickname()
        );
    }

    public MemberCreateCommand toCommand(
            Function<OidcAuthenticationRequest, ExternalPrincipal> authenticator,
            LocalDateTime submittedAt
    ) {
        return new MemberCreateCommand(
                authenticator.apply(oidcPayload.toAuthenticationRequest()),
                toPayload(),
                termAgreementRequest.toTermAgreements(submittedAt)
        );
    }
}
