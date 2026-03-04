package com.barlow.core.service.business.account.myinfo;

import com.barlow.core.domain.Passport;
import com.barlow.core.domain.account.UserQuery;
import com.barlow.core.domain.account.UserRepository;
import com.barlow.core.domain.account.authprovider.AuthProviderRepository;
import com.barlow.core.domain.account.authprovider.UserAuthProvider;
import com.barlow.core.domain.account.device.Device;
import com.barlow.core.domain.account.device.DeviceRepository;
import com.barlow.core.domain.account.myinfo.AccountProfile;
import com.barlow.core.domain.account.myinfo.MyAccountInfo;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MyAccountRetrieveService {

	private final UserRepository userRepository;
	private final AuthProviderRepository authProviderRepository;
	private final DeviceRepository deviceRepository;

	public MyAccountRetrieveService(
		UserRepository userRepository,
		AuthProviderRepository authProviderRepository,
		DeviceRepository deviceRepository
	) {
		this.userRepository = userRepository;
		this.authProviderRepository = authProviderRepository;
		this.deviceRepository = deviceRepository;
	}

	public MyAccountInfo retrieve(Passport passport) {
		long userNo = passport.getUserNo();
		UserQuery userQuery = new UserQuery(userNo);

		AccountProfile profile = userRepository.retrieveProfile(userQuery);
		UserAuthProvider userAuthProvider = authProviderRepository.retrieveByUser(userQuery);
		List<Device> devices = deviceRepository.findAllByUserNo(userNo);

		return new MyAccountInfo(profile, userAuthProvider.externalPrincipals(), devices);
	}
}
