package com.barlow.core.domain.account.create;

import com.barlow.core.domain.User;

public record UserRegisterCommand(String nickname, User.Role role) {
}
