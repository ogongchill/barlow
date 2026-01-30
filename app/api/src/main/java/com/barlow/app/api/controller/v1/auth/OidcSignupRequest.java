package com.barlow.app.api.controller.v1.auth;

import com.barlow.core.domain.registration.MemberCreateCommand;
import com.barlow.services.auth.authentication.oauth.OidcAuthenticationRequest;
import com.barlow.core.enumerate.DeviceOs;

public record OidcSignupRequest(
        OidcAuthenticationRequest oidcRequest,
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
}
