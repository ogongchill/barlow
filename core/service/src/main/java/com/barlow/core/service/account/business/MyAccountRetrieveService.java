package com.barlow.core.service.account.business;

import com.barlow.core.domain.Passport;
import com.barlow.core.domain.account.UserQuery;
import com.barlow.core.domain.account.UserRepository;
import com.barlow.core.domain.externalauth.ExternalAuthRepository;
import com.barlow.core.domain.externalauth.UserExternalAuth;
import com.barlow.core.domain.device.Device;
import com.barlow.core.domain.device.DeviceRepository;
import com.barlow.core.domain.account.AccountProfile;
import com.barlow.core.domain.account.MyAccountInfo;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MyAccountRetrieveService {

	private final UserRepository userRepository;
	private final ExternalAuthRepository authProviderRepository;
	private final DeviceRepository deviceRepository;

	public MyAccountRetrieveService(UserRepository userRepository, ExternalAuthRepository authProviderRepository,
		DeviceRepository deviceRepository) {
		this.userRepository = userRepository;
		this.authProviderRepository = authProviderRepository;
		this.deviceRepository = deviceRepository;
	}

	public MyAccountInfo retrieve(Passport passport) {
		long userNo = passport.getUserNo();
		UserQuery userQuery = new UserQuery(userNo);

		AccountProfile profile = userRepository.retrieveProfile(userQuery);
		UserExternalAuth userAuthProvider = authProviderRepository.retrieveByUser(userQuery);
		List<Device> devices = deviceRepository.findAllByUserNo(userNo);

		return new MyAccountInfo(profile, userAuthProvider.getExternalPrincipals(), devices);
	}
}
