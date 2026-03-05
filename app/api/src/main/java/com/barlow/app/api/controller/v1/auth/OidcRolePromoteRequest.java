package com.barlow.app.api.controller.v1.auth;

import com.barlow.core.domain.User;
import com.barlow.core.domain.externalauth.ExternalPrincipal;
import com.barlow.core.domain.account.MemberPromoteCommand;
import com.barlow.infra.auth.authentication.oauth.OidcAuthenticationRequest;

import java.util.function.Function;
import java.util.function.Supplier;

public record OidcRolePromoteRequest(OidcPayload oidcPayload) {
	MemberPromoteCommand toCommand(Supplier<User> userSupplier,
		Function<OidcAuthenticationRequest, ExternalPrincipal> authenticator) {
		return new MemberPromoteCommand(authenticator.apply(oidcPayload.toAuthenticationRequest()), userSupplier.get());
	}
}
