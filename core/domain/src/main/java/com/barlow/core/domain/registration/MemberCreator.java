package com.barlow.core.domain.registration;

import com.barlow.core.domain.User;
import com.barlow.core.domain.account.UserCreator;
import com.barlow.core.domain.account.UserRepository;
import com.barlow.core.domain.account.UserRoleChangeCommand;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class MemberCreator {

    private final UserCreator userCreator;
    private final AuthProviderService authProviderService;
    private final TermRepository termRepository;
    private final UserRepository userRepository;

    public MemberCreator(UserCreator userCreator, AuthProviderService authProviderService, TermRepository termRepository, UserRepository userRepository) {
        this.userCreator = userCreator;
        this.authProviderService = authProviderService;
        this.termRepository = termRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public User createMember(MemberRegistrationContext registrationContext) {
        if (!(registrationContext.target() instanceof RegistrationTarget.NewUser)) {
            throw RegistrationException.invalidFlowStatus("신규 유저가 아닙니다.");
        }
        validateTermAgreement(registrationContext);
        User user = createUser((RegistrationTarget.NewUser) registrationContext.target());
        createAuthProvider(registrationContext, user);
        saveTermAgreement(registrationContext, user);
        return user;
    }

    @Transactional
    public User promoteGuestToMember(MemberRegistrationContext registrationContext) {
        if (!(registrationContext.target() instanceof RegistrationTarget.ExistingUser existingUser)) {
            throw RegistrationException.invalidFlowStatus("이미 가입된 유저가 아닙니다.");
        }
        validateTermAgreement(registrationContext);
        createAuthProvider(registrationContext, existingUser.getUser());
        saveTermAgreement(registrationContext, existingUser.getUser());
        return userRepository.changeRole(new UserRoleChangeCommand(existingUser.getUserNo(), User.Role.MEMBER));
    }

    private List<TermAgreement> saveTermAgreement(MemberRegistrationContext registrationContext, User user) {
        UserTermAgreementCommand termCommand = new UserTermAgreementCommand(user.getUserNo(), registrationContext.agreements());
        return termRepository.saveUserAgreement(termCommand);
    }

    private UserAuthProvider createAuthProvider(MemberRegistrationContext registrationContext, User user) {
        UserAuthProviderCreateCommand command = UserAuthProviderCreateCommand.from(
                registrationContext.principal(),
                user.getUserNo()
        );
        return authProviderService.create(command);
    }

    private User createUser(RegistrationTarget.NewUser target) {
        return userCreator.create(target.toCommand());
    }

    private void validateTermAgreement(MemberRegistrationContext registrationContext) {
        List<Term> requirements = termRepository.retrieveRequirements();
        TermsPolicy policy = TermsPolicy.from(requirements);
        policy.validate(registrationContext.agreements());
    }
}
