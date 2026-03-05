package com.barlow.core.service.account.business;

import com.barlow.core.domain.User;
import com.barlow.core.domain.account.UserQuery;
import com.barlow.core.domain.account.UserRepository;
import com.barlow.core.domain.externalauth.UserExternalAuth;
import com.barlow.core.domain.externalauth.ExternalAuthCreateCommand;
import com.barlow.core.domain.account.GuestToMemberCommand;
import com.barlow.core.domain.account.MemberCreateCommand;
import com.barlow.core.domain.account.MemberPromoteCommand;
import com.barlow.core.service.account.impl.TermManager;
import com.barlow.core.service.account.impl.UserCreator;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberRegisterService {

	private final UserCreator userCreator;
	private final TermManager termManager;
	private final ExternalAuthService authProviderService;
	private final UserRepository userRepository;

	public MemberRegisterService(UserCreator userCreator, TermManager termManager,
		ExternalAuthService authProviderService, UserRepository userRepository
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
		authProviderService.create(
			new ExternalAuthCreateCommand(
				command.externalPrincipal().authProvider(), command.externalPrincipal().sub(), user.getUserNo()));
		return user;
	}

	@Transactional
	public User promoteToMember(MemberPromoteCommand command) {
		User existingUser = userRepository.retrieve(new UserQuery(command.user().getUserNo()));
		GuestToMemberCommand promoteCommand = existingUser.toGuestToMemberCommand();
		UserExternalAuth existingAuthProvider = authProviderService.retrieveAll(new UserQuery(existingUser.getUserNo()));
		authProviderService.create(existingAuthProvider.toCommand(command.principal()));
		return userRepository.promoteToMember(promoteCommand);
	}
}
