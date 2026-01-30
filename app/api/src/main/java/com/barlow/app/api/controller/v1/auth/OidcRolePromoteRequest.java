package com.barlow.app.api.controller.v1.auth;

public record OidcRolePromoteRequest(
        TermAgreementRequest termAgreementRequest,
        OidcRequest oidcRequest
) {
}
