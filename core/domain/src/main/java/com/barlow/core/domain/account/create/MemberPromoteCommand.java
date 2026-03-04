package com.barlow.core.domain.account.create;

import com.barlow.core.domain.User;
import com.barlow.core.domain.account.authprovider.ExternalPrincipal;

public record MemberPromoteCommand(ExternalPrincipal principal, User user) {
}
