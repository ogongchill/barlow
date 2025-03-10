package com.barlow.storage.db.core;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.barlow.core.enumerate.LegislationType;
import com.barlow.storage.db.CoreDbContextTest;
import com.barlow.storage.db.support.StorageTest;

import jakarta.transaction.Transactional;

@StorageTest("dummy/legislationAccount.json")
class LegislationRepositoryAdapterTest extends CoreDbContextTest {

	private final LegislationRepositoryAdapter adapter;
	private final LegislationAccountJpaRepository legislationAccountJpaRepository;

	public LegislationRepositoryAdapterTest(
		LegislationRepositoryAdapter adapter,
		LegislationAccountJpaRepository legislationAccountJpaRepository
	) {
		this.adapter = adapter;
		this.legislationAccountJpaRepository = legislationAccountJpaRepository;
	}

	@DisplayName("상임위원회 계정을 조회한다")
	@Test
	void retrieve() {
		assertThat(adapter.retrieve(LegislationType.HOUSE_STEERING)).isNotNull();
	}

	@DisplayName("모든 입법계정에서 모든 상임위원회 계정을 조회한다")
	@Test
	void retrieveCommitteeAccount() {
		assertThat(adapter.retrieveCommitteeAccount())
			.isNotEmpty()
			.hasSize(2);
	}

	@DisplayName("상임위원회 계정 구독자를 1만큼 증가시킨다")
	@Test
	@Transactional
	void incrementSubscriber() {
		LegislationType houseSteering = LegislationType.HOUSE_STEERING;
		adapter.incrementSubscriber(houseSteering);

		assertThat(legislationAccountJpaRepository.findByNo(houseSteering.getLegislationNo())
			.toLegislationAccount()
			.getSubscriberCount()
		).isEqualTo(301);
	}

	@DisplayName("상임위원회 계정 구독자를 1만큼 감소시킨다")
	@Test
	@Transactional
	void decrementSubscriber() {
		LegislationType houseSteering = LegislationType.HOUSE_STEERING;
		adapter.decrementSubscriber(houseSteering);

		assertThat(legislationAccountJpaRepository.findByNo(houseSteering.getLegislationNo())
			.toLegislationAccount()
			.getSubscriberCount()
		).isEqualTo(299);
	}
}