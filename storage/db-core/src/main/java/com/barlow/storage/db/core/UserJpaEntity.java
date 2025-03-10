package com.barlow.storage.db.core;

import com.barlow.core.domain.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "barlow_user")
public class UserJpaEntity extends BaseTimeJpaEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long no;

	@Column(name = "nickname", nullable = false)
	private String nickname;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "varchar(10)", name = "role", nullable = false)
	private User.Role role;

	protected UserJpaEntity() {
	}

	private UserJpaEntity(String nickname, User.Role role) {
		this.nickname = nickname;
		this.role = role;
	}

	User toUser() {
		return User.of(no, role);
	}

	Long getNo() {
		return no;
	}

	static UserJpaEntity guestOf(String nickname) {
		return new UserJpaEntity(nickname, User.Role.GUEST);
	}

	static UserJpaEntity memberOf(String nickname) {
		return new UserJpaEntity(nickname, User.Role.MEMBER);
	}
}
