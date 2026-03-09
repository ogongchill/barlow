package com.barlow.core.domain.account;

import com.barlow.core.domain.User;

public record UserRegisterCommand(String nickname, User.Role role) {
}
