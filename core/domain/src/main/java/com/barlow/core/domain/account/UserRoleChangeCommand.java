package com.barlow.core.domain.account;

import com.barlow.core.domain.User;

public record UserRoleChangeCommand(
        Long userNo,
        User.Role role
) {
}
