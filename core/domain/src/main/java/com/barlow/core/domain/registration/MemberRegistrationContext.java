package com.barlow.core.domain.registration;

import java.util.List;

public record MemberRegistrationContext(
        ExternalPrincipal principal,
        RegistrationTarget target,
        List<TermAgreement> agreements
) {

    public static MemberRegistrationContext withNewUser(ExternalPrincipal externalPrincipal, RegistrationTarget.NewUser target) {
        return new MemberRegistrationContext(externalPrincipal, target, List.of());
    }

    public static MemberRegistrationContext withExistingUser(ExternalPrincipal externalPrincipal, RegistrationTarget.ExistingUser target) {
        return new MemberRegistrationContext(externalPrincipal, target, List.of());
    }

    public MemberRegistrationContext withAgreements(List<TermAgreement> agreements) {
        return new MemberRegistrationContext(principal, target, List.copyOf(agreements));
    }
}
