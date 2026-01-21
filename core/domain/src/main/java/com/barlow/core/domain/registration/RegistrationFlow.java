package com.barlow.core.domain.registration;

import com.barlow.core.domain.User;

import java.util.List;

public class RegistrationFlow {

    private final ExternalPrincipal externalPrincipal;
    private final RegistrationTarget target;
    private final List<TermAgreement> termAgreements;
    private final Status status;

    private RegistrationFlow(ExternalPrincipal externalPrincipal, RegistrationTarget target, List<TermAgreement> termAgreements, Status status) {
        this.externalPrincipal = externalPrincipal;
        this.target = target;
        this.termAgreements = termAgreements;
        this.status = status;
    }

    public static RegistrationFlow fromNewUser(ExternalPrincipal externalPrincipal) {
        String defaultNickname = externalPrincipal.authProvider().getIssuer() + externalPrincipal.sub();
        return new RegistrationFlow(
                externalPrincipal,
                new RegistrationTarget.NewUser(defaultNickname),
                List.of(),
                Status.AUTHENTICATED
        );
    }

    public static RegistrationFlow fromExistingUser(ExternalPrincipal externalPrincipal, User user) {
        return new RegistrationFlow(
                externalPrincipal,
                RegistrationTarget.ExistingUser.from(user),
                List.of(),
                Status.AUTHENTICATED
        );
    }

    public RegistrationFlow agreeTerms(List<TermAgreement> agreements, TermsPolicy policy) {
        policy.validate(agreements);
        return new RegistrationFlow(
                externalPrincipal,
                target,
                List.copyOf(agreements),
                Status.TERMS_AGREED
        );
    }

    public RegistrationFlow changeNickname(String nickname) {
        if(!(target instanceof RegistrationTarget.NewUser)) {
            throw RegistrationException.InvalidFlowStatusException("신규 유저만 닉네임을 설정 할 수 있습니다.");
        }

        return new RegistrationFlow(
                externalPrincipal,
                new RegistrationTarget.NewUser(nickname),
                termAgreements,
                status
        );
    }

    public RegistrationFlow complete() {
        if(this.status != Status.TERMS_AGREED) {
            throw RegistrationException.InvalidFlowStatusException("약관 동의 상태가 아닙니다.");
        }

        return new RegistrationFlow(
                externalPrincipal,
                target,
                termAgreements,
                Status.REGISTERED
        );
    }

    public Status getStatus() {
        return status;
    }

    public List<TermAgreement> getTermAgreements() {
        return termAgreements;
    }

    public RegistrationTarget getTarget() {
        return target;
    }

    public ExternalPrincipal getExternalPrincipal() {
        return externalPrincipal;
    }

    public enum Status {

        AUTHENTICATED,
        TERMS_AGREED,
        REGISTERED,
    }
}
