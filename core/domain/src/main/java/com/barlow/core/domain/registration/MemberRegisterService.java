package com.barlow.core.domain.registration;

import com.barlow.core.domain.User;
import com.barlow.core.domain.account.UserCreator;
import com.barlow.core.domain.account.UserQuery;
import com.barlow.core.domain.account.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class MemberRegisterService {

    private final UserCreator userCreator;
    private final TermManager termManager;
    private final AuthProviderService authProviderService;
    private final UserRepository userRepository;

    public MemberRegisterService(
            UserCreator userCreator,
            TermManager termManager,
            AuthProviderService authProviderService,
            UserRepository userRepository
    ) {
        this.userCreator = userCreator;
        this.termManager = termManager;
        this.authProviderService = authProviderService;
        this.userRepository = userRepository;
    }

    @Transactional
    public User createNewMember(MemberCreateCommand command) {
        termManager.validateAgreements(command.agreements());
        User user = userCreator.create(command.toUserCreateCommand());
        termManager.saveAgreements(command.agreements(), user);
        authProviderService.create(new UserAuthProviderCreateCommand(command.externalPrincipal().authProvider(), command.externalPrincipal().sub(), user.getUserNo()));
        return user;
    }

    @Transactional
    public User promoteToMember(MemberPromoteCommand command) {
        User existingUser = userRepository.retrieve(new UserQuery(command.user().getUserNo()));
        GuestToMemberCommand promoteCommand = existingUser.toGuestToMemberCommand();
        termManager.validateAgreements(command.agreements());
        termManager.saveAgreements(command.agreements(), existingUser);
        authProviderService.create(new UserAuthProviderCreateCommand(command.principal().authProvider(), command.principal().sub(), existingUser.getUserNo()));
        return userRepository.promoteToMember(promoteCommand);
    }

}
