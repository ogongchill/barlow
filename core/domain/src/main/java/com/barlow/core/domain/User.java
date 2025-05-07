package com.barlow.core.domain;

import java.util.Objects;

public class User {

	private final long userNo;
	private final Role role;

	private User(long userNo, Role role) {
		this.userNo = userNo;
		this.role = role;
	}

	public static User of(Long userNo, String role) {
		return new User(userNo, Role.valueOf(role.toUpperCase()));
	}

	public static User of(Long userNo, Role role) {
		return new User(userNo, role);
	}

	public boolean isMemberUser() {
		return this.role == Role.MEMBER;
	}

	public boolean isGuestUser() {
		return this.role == Role.GUEST;
	}

	public boolean isAdminUser() {
		return this.role == Role.ADMIN;
	}

	public long getUserNo() {
		return userNo;
	}

	public String getRoleName() {
		return role.name();
	}

	public enum Role {
		ADMIN, MEMBER, GUEST;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		User user = (User)o;
		return userNo == user.userNo && role == user.role;
	}

	@Override
	public int hashCode() {
		return Objects.hash(userNo, role);
	}
}
