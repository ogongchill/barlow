package com.barlow.storage.db.core;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.barlow.core.domain.User;
import com.barlow.core.domain.subscribe.Subscribe;
import com.barlow.core.domain.subscribe.SubscribeQuery;
import com.barlow.core.enumerate.LegislationType;
import com.barlow.storage.db.CoreDbContextTest;
import com.barlow.storage.db.support.StorageTest;

import jakarta.transaction.Transactional;

@StorageTest("dummy/subscribe.json")
class SubscribeRepositoryAdapterTest extends CoreDbContextTest {

	private static final User GUEST_USER = User.of(1L, User.Role.GUEST);

	private final SubscribeRepositoryAdapter adapter;
	private final SubscribeJpaRepository subscribeJpaRepository;

	public SubscribeRepositoryAdapterTest(
		SubscribeRepositoryAdapter adapter,
		SubscribeJpaRepository subscribeJpaRepository
	) {
		this.adapter = adapter;
		this.subscribeJpaRepository = subscribeJpaRepository;
	}

	@DisplayName("입법계정 정보를 통해 회원이 해당 입법계정에 대해 구독했는지 조회한다")
	@ParameterizedTest
	@CsvSource(value = {"HOUSE_STEERING:true", "INTELLIGENCE:false"}, delimiter = ':')
	void retrieve(String legislationTypeName, boolean expect) {
		Subscribe retrieve = adapter.retrieve(
			new SubscribeQuery(LegislationType.valueOf(legislationTypeName), GUEST_USER)
		);

		assertAll(
			() -> assertThat(retrieve).isNotNull(),
			() -> assertThat(retrieve.isActive()).isEqualTo(expect)
		);
	}

	@DisplayName("회원이 입법계정에 대해 설정한 모든 구독 정보를 조회한다")
	@Test
	void retrieveAll() {
		assertThat(adapter.retrieveAll(GUEST_USER))
			.isNotEmpty()
			.hasSize(18);
	}

	@DisplayName("회원이 입법계정 정보를 통해 새롭게 해당 입법계정을 구독한다")
	@Test
	@Transactional
	void save() {
		LegislationType intelligence = LegislationType.INTELLIGENCE;
		adapter.save(new Subscribe(GUEST_USER, intelligence.getLegislationNo(), intelligence, true));

		assertThat(subscribeJpaRepository.findAllByMemberNo(GUEST_USER.getUserNo()))
			.isNotEmpty()
			.hasSize(2);
	}

	@DisplayName("회원이 입법계정 정보를 통해 새롭게 해당 입법계정을 구독을 삭제한다")
	@Test
	@Transactional
	void delete() {
		LegislationType houseSteering = LegislationType.HOUSE_STEERING;
		adapter.delete(new Subscribe(GUEST_USER, houseSteering.getLegislationNo(), houseSteering, false));

		assertThat(subscribeJpaRepository.findAllByMemberNo(GUEST_USER.getUserNo()))
			.isEmpty();
	}
}