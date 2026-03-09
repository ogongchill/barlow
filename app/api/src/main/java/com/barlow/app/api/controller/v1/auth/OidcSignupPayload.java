package com.barlow.app.api.controller.v1.auth;

import com.barlow.app.support.error.CoreApiException;
import com.barlow.app.support.validate.Validatable;

public record OidcSignupPayload(String deviceOs, String deviceId, String deviceToken,
	String nickname) implements Validatable {

	@Override
	public void validate() {
		if (deviceId == null || deviceId.isBlank()) {
			throw CoreApiException.badRequest("Device ID cannot be null or empty");
		}
		if (deviceToken == null || deviceToken.isBlank()) {
			throw CoreApiException.badRequest("Device token cannot be null or empty");
		}
		if (deviceOs == null || deviceOs.isBlank()) {
			throw CoreApiException.badRequest("Device Os cannot be null or empty");
		}
		if (nickname == null || nickname.isBlank()) {
			throw CoreApiException.badRequest("Nickname cannot be null or empty");
		}
		if (!deviceOs.matches("^(?i)(ios|android)$")) {
			throw CoreApiException.badRequest("os 는 대소문자 관계 없이 'ios' 와 'android' 타입만 허용됨");
		}
	}
}
