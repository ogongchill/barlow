package com.barlow.core.domain.account.create;

import com.barlow.core.domain.User;
import com.barlow.core.domain.account.authprovider.ExternalPrincipal;
import com.barlow.core.domain.account.term.TermAgreement;
import com.barlow.core.enumerate.DeviceOs;

import java.util.List;

public record MemberCreateCommand(
        ExternalPrincipal externalPrincipal,
        UserPayload payload,
        List<TermAgreement> agreements
) {
    public record UserPayload(
            DeviceOs os,
            String deviceId,
            String deviceToken,
            String nickname
    ) {
    }

    public UserCreateCommand toUserCreateCommand() {
        return new UserCreateCommand(
                payload.os,
                payload.deviceId,
                payload.deviceToken,
                payload().nickname,
                User.Role.MEMBER
        );
    }
}
