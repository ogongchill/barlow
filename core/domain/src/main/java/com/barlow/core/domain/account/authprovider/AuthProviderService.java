package com.barlow.core.domain.account.authprovider;

import com.barlow.core.domain.account.RegistrationException;
import com.barlow.core.domain.account.UserQuery;
import org.springframework.stereotype.Component;

@Component
public class AuthProviderService {

    private final AuthProviderRepository authProviderRepository;

    public AuthProviderService(AuthProviderRepository authProviderRepository) {
        this.authProviderRepository = authProviderRepository;
    }

    public UserAuthProvider retrieveAll(UserQuery query) {
        return authProviderRepository.retrieveByUser(query);
    }

    public UserAuthProvider create(UserAuthProviderCreateCommand command) {
        if (authProviderRepository.existsByUserIdAndProvider(command)) {
            throw RegistrationException.authProviderExists(command.authProvider());
        }
        if (authProviderRepository.existsByProviderAndSub(command)) {
            throw RegistrationException.oauthAlreadyRegistered();
        }
        return authProviderRepository.create(command);
    }
}
