package com.barlow.core.domain.account;

import com.barlow.core.domain.User;
import com.barlow.core.domain.externalauth.ExternalPrincipal;

public record MemberPromoteCommand(ExternalPrincipal principal, User user) {
}
