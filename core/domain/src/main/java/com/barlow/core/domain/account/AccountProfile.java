package com.barlow.core.domain.account;

import com.barlow.core.domain.User;

public record AccountProfile(long userNo, String nickname, User.Role role) {
}
