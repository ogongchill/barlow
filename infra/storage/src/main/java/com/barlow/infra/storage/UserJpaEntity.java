package com.barlow.infra.storage;

import com.barlow.core.domain.User;

import com.barlow.core.domain.account.UserRegisterCommand;
import com.barlow.core.domain.account.AccountProfile;
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

	protected UserJpaEntity() {}

	private UserJpaEntity(String nickname, User.Role role) {
		this.nickname = nickname;
		this.role = role;
	}

	User toUser() {
		return User.of(no, role);
	}

	AccountProfile toAccountProfile() {
		return new AccountProfile(no, nickname, role);
	}

	Long getNo() {
		return no;
	}

	public static UserJpaEntity fromCommand(UserRegisterCommand command) {
		return new UserJpaEntity(command.nickname(), command.role());
	}

	public static UserJpaEntity guestOf(String nickname) {
		return new UserJpaEntity(nickname, User.Role.GUEST);
	}

	public static UserJpaEntity memberOf(String nickname) {
		return new UserJpaEntity(nickname, User.Role.MEMBER);
	}
}
