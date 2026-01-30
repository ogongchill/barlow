package com.barlow.core.domain.registration;

import com.barlow.core.domain.User;

import java.util.List;

public record MemberPromoteCommand(
        ExternalPrincipal principal,
        User user,
        List<TermAgreement> agreements
) {
}
