package com.barlow.core.domain;

import java.util.Objects;

public class User {

	private final long userNo;
	private final String userId;
	private final Role role;

	private User(long userNo, String userId, Role role) {
		this.userNo = userNo;
		this.userId = userId;
		this.role = role;
	}

	public static User authorizedOf(Long userNo, String userId) {
		return new User(userNo, userId, Role.AUTHORIZED_USER);
	}

	public static User unAuthorizedOf(Long userNo, String userId) {
		return new User(userNo, userId, Role.UNAUTHORIZED_USER);
	}

	boolean isAuthorizedUser() {
		return this.role == Role.AUTHORIZED_USER;
	}

	boolean isUnauthorizedUser() {
		return this.role == Role.UNAUTHORIZED_USER;
	}

	public long getUserNo() {
		return userNo;
	}

	enum Role {
		ADMIN, AUTHORIZED_USER, UNAUTHORIZED_USER;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		User user = (User)o;
		return userNo == user.userNo && Objects.equals(userId, user.userId) && role == user.role;
	}

	@Override
	public int hashCode() {
		return Objects.hash(userNo, userId, role);
	}
}
