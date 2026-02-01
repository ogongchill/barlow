package com.barlow.core.domain.account.create;

import com.barlow.core.domain.User;
import com.barlow.core.domain.account.authprovider.ExternalPrincipal;
import com.barlow.core.domain.account.term.TermAgreement;

import java.util.List;

public record MemberPromoteCommand(
        ExternalPrincipal principal,
        User user,
        List<TermAgreement> agreements
) {
}
