package com.barlow.core.service.account.business;

import com.barlow.core.domain.account.RegistrationException;
import com.barlow.core.domain.account.UserQuery;
import com.barlow.core.domain.externalauth.ExternalAuthRepository;
import com.barlow.core.domain.externalauth.UserExternalAuth;
import com.barlow.core.domain.externalauth.ExternalAuthCreateCommand;
import org.springframework.stereotype.Component;

@Component
public class ExternalAuthService {

	private final ExternalAuthRepository authProviderRepository;

	public ExternalAuthService(ExternalAuthRepository authProviderRepository) {
		this.authProviderRepository = authProviderRepository;
	}

	public UserExternalAuth retrieveAll(UserQuery query) {
		return authProviderRepository.retrieveByUser(query);
	}

	public UserExternalAuth create(ExternalAuthCreateCommand command) {
		if (authProviderRepository.existsByProviderAndSub(command)) {
			throw RegistrationException.oauthAlreadyRegistered();
		}
		return authProviderRepository.create(command);
	}
}
