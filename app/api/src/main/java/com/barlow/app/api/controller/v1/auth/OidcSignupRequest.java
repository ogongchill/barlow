package com.barlow.app.api.controller.v1.auth;

import com.barlow.core.domain.account.authprovider.ExternalPrincipal;
import com.barlow.core.domain.account.create.MemberCreateCommand;
import com.barlow.core.domain.account.term.TermAgreement;
import com.barlow.core.enumerate.DeviceOs;
import com.barlow.services.auth.authentication.oauth.OidcAuthenticationRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public record OidcSignupRequest(
    OidcPayload oidcPayload,
    Map<Long, Boolean> termAgreements,
    OidcSignupPayload signupPayload
) {
    public MemberCreateCommand.UserPayload toPayload() {
        return new MemberCreateCommand.UserPayload(
                DeviceOs.valueOf(signupPayload.deviceOs().toUpperCase()),
                signupPayload().deviceId(),
                signupPayload().deviceToken(),
                signupPayload.nickname()
        );
    }

    public MemberCreateCommand toCommand(
            Function<OidcAuthenticationRequest, ExternalPrincipal> authenticator,
            LocalDateTime submittedAt
    ) {
        return new MemberCreateCommand(
                authenticator.apply(oidcPayload.toAuthenticationRequest()),
                toPayload(),
                toTermAgreementList(submittedAt)
        );
    }

    private List<TermAgreement> toTermAgreementList(LocalDateTime submittedAt) {
        return termAgreements.entrySet().stream()
                .map(entry -> Boolean.TRUE.equals(entry.getValue())
                        ? TermAgreement.agreedAt(entry.getKey(), submittedAt)
                        : TermAgreement.disagreedAt(entry.getKey(), submittedAt))
                .toList();
    }
}
