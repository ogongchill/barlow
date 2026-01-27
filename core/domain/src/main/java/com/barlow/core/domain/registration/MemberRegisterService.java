package com.barlow.core.domain.registration;

import com.barlow.core.domain.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MemberRegisterService {

    private final MemberCreator memberCreator;

    public MemberRegisterService(MemberCreator memberCreator) {
        this.memberCreator = memberCreator;
    }

    public User createNewMember(
            ExternalPrincipal externalPrincipal,
            List<TermAgreement> agreements,
            RegistrationTarget.NewUser newUser
    ) {
        MemberRegistrationContext context = MemberRegistrationContext.withNewUser(externalPrincipal, newUser)
                .withAgreements(agreements);
        return memberCreator.createMember(context);
    }

    public User promoteToMember(
            ExternalPrincipal externalPrincipal,
            List<TermAgreement> agreements,
            User existingUser
    ) {
        MemberRegistrationContext context = MemberRegistrationContext.withExistingUser(externalPrincipal, RegistrationTarget.ExistingUser.from(existingUser))
                .withAgreements(agreements);
        return memberCreator.promoteGuestToMember(context);
    }
}
