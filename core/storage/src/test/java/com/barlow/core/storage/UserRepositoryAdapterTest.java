package com.barlow.core.storage;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.barlow.core.domain.User;
import com.barlow.core.domain.account.UserQuery;
import com.barlow.core.domain.account.UserRegisterCommand;
import com.barlow.core.storage.support.StorageTest;

@StorageTest("dummy/user.json")
class UserRepositoryAdapterTest extends CoreDbContextTest {

	private final UserRepositoryAdapter adapter;
	private final UserRepositoryJpaRepository userRepositoryJpaRepository;

	public UserRepositoryAdapterTest(
		UserRepositoryAdapter adapter,
		UserRepositoryJpaRepository userRepositoryJpaRepository
	) {
		this.adapter = adapter;
		this.userRepositoryJpaRepository = userRepositoryJpaRepository;
	}

	@DisplayName("등록되어 있는 회원을 조회한다")
	@Test
	void retrieve() {
		assertThat(adapter.retrieve(new UserQuery(1L)))
			.isNotNull();
	}

	@DisplayName("새로운 회원을 등록하면 게스트 회원으로 저장한다")
	@Test
	void create() {
		User user = adapter.create(
			new UserRegisterCommand("newNickname")
		);

		assertAll(
			() -> assertThat(user).isNotNull(),
			() -> assertThat(user.isGuestUser()).isTrue(),
			() -> assertThat(userRepositoryJpaRepository.count()).isEqualTo(2)
		);
	}
}