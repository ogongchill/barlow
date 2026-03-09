package com.barlow.app.api.controller.v1.account;

import com.barlow.core.domain.externalauth.ExternalPrincipal;
import com.barlow.core.domain.device.Device;
import com.barlow.core.domain.account.AccountProfile;
import com.barlow.core.domain.account.MyAccountInfo;

import java.util.List;

public record MyAccountResponse(UserInfo user, List<AuthProviderInfo> authProviders, List<DeviceInfo> devices) {

	public static MyAccountResponse from(MyAccountInfo myAccountInfo) {
		return new MyAccountResponse(
			UserInfo.from(myAccountInfo.profile()),
			myAccountInfo.authProviders().stream().map(AuthProviderInfo::from).toList(),
			myAccountInfo.devices().stream().map(DeviceInfo::from).toList());
	}

	public record UserInfo(long userNo, String nickname, String role) {
		static UserInfo from(AccountProfile profile) {
			return new UserInfo(profile.userNo(), profile.nickname(), profile.role().name());
		}
	}

	public record AuthProviderInfo(String provider, String sub) {
		static AuthProviderInfo from(ExternalPrincipal principal) {
			return new AuthProviderInfo(principal.authProvider().name(), principal.sub());
		}
	}

	public record DeviceInfo(String deviceId, String deviceOs, String status) {
		static DeviceInfo from(Device device) {
			return new DeviceInfo(device.getDeviceId(), device.getDeviceOs().name(), device.getStatus().name());
		}
	}
}
