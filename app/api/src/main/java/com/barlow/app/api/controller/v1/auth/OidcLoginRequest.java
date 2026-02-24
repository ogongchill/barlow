package com.barlow.app.api.controller.v1.auth;

import com.barlow.app.support.error.CoreApiException;
import com.barlow.app.support.validate.Validatable;
import com.barlow.core.domain.account.login.MemberLoginCommand;
import com.barlow.core.domain.account.authprovider.ExternalPrincipal;
import com.barlow.core.enumerate.DeviceOs;
import com.barlow.services.auth.authentication.oauth.OidcAuthenticationRequest;

import java.util.function.Function;

public record OidcLoginRequest (
    String deviceOs,
    String deviceId,
    String deviceToken,
    OidcPayload oidcPayload
) implements Validatable {

    MemberLoginCommand toCommand(Function<OidcAuthenticationRequest, ExternalPrincipal> authenticator) {
        return new MemberLoginCommand(
                deviceId,
                DeviceOs.valueOf(deviceOs.toUpperCase()),
                deviceToken,
                authenticator.apply(oidcPayload.toAuthenticationRequest())
        );
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
        if (!deviceOs.matches("^(?i)(ios|android)$")) {
            throw CoreApiException.badRequest("os 는 대소문자 관계 없이 'ios' 와 'android' 타입만 허용됨");
        }
    }
}
