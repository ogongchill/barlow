package com.barlow.infra.storage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.barlow.core.domain.account.UserQuery;
import com.barlow.core.domain.account.authprovider.UserAuthProvider;
import com.barlow.core.domain.account.authprovider.UserAuthProviderCreateCommand;
import com.barlow.core.enumerate.AuthProvider;
import com.barlow.infra.storage.support.StorageTest;

@DataJpaTest
@StorageTest(value = "dummy/authProvider.json", dbType = StorageTest.DatabaseType.H2)
@Import(AuthProviderRepositoryAdapter.class)
class AuthProviderRepositoryAdapterTest {

	@Autowired
	private AuthProviderRepositoryAdapter adapter;

	@Autowired
	private AuthProviderJpaRepository authProviderJpaRepository;

	@DisplayName("새로운 인증 제공자 정보를 저장한다")
	@Test
	void create() {
		UserAuthProviderCreateCommand command = new UserAuthProviderCreateCommand(
			AuthProvider.KAKAO, "new_kakao_sub", 3L);

		UserAuthProvider result = adapter.create(command);

		assertAll(
			() -> assertThat(result).isNotNull(), () -> assertThat(result.userNo()).isEqualTo(3L),
			() -> assertThat(result.externalPrincipals()).hasSize(1),
			() -> assertThat(authProviderJpaRepository.count()).isEqualTo(4));
	}

	@DisplayName("제공자와 sub로 인증 제공자 존재 여부를 확인한다 - 존재하는 경우")
	@Test
	void existsByProviderAndSub_exists() {
		UserAuthProviderCreateCommand command = new UserAuthProviderCreateCommand(
			AuthProvider.KAKAO, "kakao_sub_1", 1L);

		boolean exists = adapter.existsByProviderAndSub(command);

		assertThat(exists).isTrue();
	}

	@DisplayName("제공자와 sub로 인증 제공자 존재 여부를 확인한다 - 존재하지 않는 경우")
	@Test
	void existsByProviderAndSub_notExists() {
		UserAuthProviderCreateCommand command = new UserAuthProviderCreateCommand(
			AuthProvider.KAKAO, "non_existent_sub", 1L);

		boolean exists = adapter.existsByProviderAndSub(command);

		assertThat(exists).isFalse();
	}

	@DisplayName("사용자 ID와 제공자로 인증 제공자 존재 여부를 확인한다 - 존재하는 경우")
	@Test
	void existsByUserIdAndProvider_exists() {
		UserAuthProviderCreateCommand command = new UserAuthProviderCreateCommand(AuthProvider.KAKAO, "any_sub", 1L);

		boolean exists = adapter.existsByUserIdAndProvider(command);

		assertThat(exists).isTrue();
	}

	@DisplayName("사용자 ID와 제공자로 인증 제공자 존재 여부를 확인한다 - 존재하지 않는 경우")
	@Test
	void existsByUserIdAndProvider_notExists() {
		UserAuthProviderCreateCommand command = new UserAuthProviderCreateCommand(AuthProvider.NAVER, "any_sub", 2L);

		boolean exists = adapter.existsByUserIdAndProvider(command);

		assertThat(exists).isFalse();
	}

	@DisplayName("사용자 번호로 인증 제공자 목록을 조회한다")
	@Test
	void retrieveByUser() {
		UserAuthProvider result = adapter.retrieveByUser(new UserQuery(1L));

		assertAll(
			() -> assertThat(result).isNotNull(), () -> assertThat(result.userNo()).isEqualTo(1L),
			() -> assertThat(result.externalPrincipals()).hasSize(2));
	}

	@DisplayName("인증 제공자가 없는 사용자를 조회하면 빈 목록을 반환한다")
	@Test
	void retrieveByUser_empty() {
		UserAuthProvider result = adapter.retrieveByUser(new UserQuery(999L));

		assertAll(
			() -> assertThat(result).isNotNull(), () -> assertThat(result.userNo()).isEqualTo(999L),
			() -> assertThat(result.externalPrincipals()).isEmpty());
	}
}
