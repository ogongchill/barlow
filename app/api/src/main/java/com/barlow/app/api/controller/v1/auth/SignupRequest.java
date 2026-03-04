package com.barlow.app.api.controller.v1.auth;

import com.barlow.app.support.error.CoreApiException;
import com.barlow.core.domain.User;
import com.barlow.core.domain.account.create.UserCreateCommand;
import com.barlow.core.domain.account.term.TermAgreement;
import com.barlow.core.enumerate.DeviceOs;
import com.barlow.app.support.validate.Validatable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public record SignupRequest(String deviceOs, String deviceId, String deviceToken, String nickname,
	Map<Long, Boolean> termAgreements) implements Validatable {

	UserCreateCommand toGuestCommand() {
		return new UserCreateCommand(
			DeviceOs.valueOf(deviceOs.toUpperCase()), deviceId, deviceToken, nickname, User.Role.GUEST);
	}

	List<TermAgreement> toTermAgreements(LocalDateTime submittedAt) {
		return termAgreements.entrySet().stream()
			.map(
				entry -> Boolean.TRUE.equals(entry.getValue()) ? TermAgreement.agreedAt(entry.getKey(), submittedAt)
					: TermAgreement.disagreedAt(entry.getKey(), submittedAt))
			.toList();
	}

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
			throw CoreApiException.badRequest("Device Os cannot be null or empty");
		}
		if (!deviceOs.matches("^(?i)(ios|android)$")) {
			throw CoreApiException.badRequest("os 는 대소문자 관계 없이 'ios' 와 'android' 타입만 허용됨");
		}
		if (termAgreements == null) {
			throw CoreApiException.badRequest("Term agreement cannot be null");
		}
	}
}
